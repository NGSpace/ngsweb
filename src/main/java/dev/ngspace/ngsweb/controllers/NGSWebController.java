package dev.ngspace.ngsweb.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.ngspace.ngsweb.WebConfig;
import dev.ngspace.ngsweb.PageServer;
import dev.ngspace.ngsweb.PageServerProcessor;
import dev.ngspace.ngsweb.exceptions.LegitFuckupException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class NGSWebController {
    
	public static final Logger logger = LoggerFactory.getLogger(NGSWebController.class);
	public WebConfig appProperties;
	public Map<String, PageServer> desktopPageServers = new HashMap<String, PageServer>();
	public Map<String, PageServer> mobilePageServers = new HashMap<String, PageServer>() {
		private static final long serialVersionUID = -2114763456002592192L;

		@Override public PageServer get(Object key) {
			PageServer mobilevers = super.get(key);
			if (appProperties.getMobileDefaultsToDesktop()&&mobilevers==null)
				return desktopPageServers.get(key);
			return mobilevers;
		}
	};

    public NGSWebController(WebConfig webconf) {
    	this.appProperties = webconf;
    	for (var entry : webconf.getDesktopWebstructure().entrySet()) {
    		logger.info("Processing desktop pageserver: " + entry.getKey());
    		desktopPageServers.put(entry.getKey(), PageServerProcessor.createPageServer(webconf, entry.getValue(), entry.getKey()));
    	}
    	
    	if (webconf.getMobileWebstructure()==null)
    		return;
    	for (var entry : webconf.getMobileWebstructure().entrySet()) {
    		logger.info("Processing mobile pageserver: " + entry.getKey());
    		mobilePageServers.put(entry.getKey(), PageServerProcessor.createPageServer(webconf, entry.getValue(), entry.getKey()));
    	}
    }
    
	@GetMapping("/404")
    public ResponseEntity<byte[]> erorr404(HttpServletRequest request) throws Exception {
        HttpHeaders headers = new HttpHeaders();
		PageServer server = desktopPageServers.get("/404");

        headers.add(HttpHeaders.CONTENT_TYPE, server.getContentType(request));
        
		try {
			return new ResponseEntity<byte[]>(server.getContent(request), headers, HttpStatus.NOT_FOUND);
		} catch (IOException e) {
			// Start praying to god there won't be an error loop :)
			throw new LegitFuckupException(e);
		}
    }
	
	@GetMapping("/generic_error")
	public ResponseEntity<String> errorGeneric(HttpServletRequest request) {
        String message = "<html><body><h1>Error</h1><p>Something went wrong.</p></body></html>";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_HTML);

        return new ResponseEntity<>(message, headers, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@GetMapping("/**")
    public ResponseEntity<byte[]> customContentType(HttpServletRequest request) throws Exception {
    	
    	logUserInfo(request);
    	
		if (!request.getRequestURI().endsWith("/")&&isSubFolderCatchAll(request)) {
			String redirect = request.getRequestURI() + "/";
			
			return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
				.header(HttpHeaders.LOCATION, redirect+(request.getQueryString()!=null?"?"+request.getQueryString():""))
				.body(("Redirecting to " + redirect).getBytes());
		}
    	
    	if (request.getRequestURI().contains("..")) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, "text/html");
            return new ResponseEntity<byte[]>("Fuck off bitch.".getBytes(),
            		headers, HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    	
        HttpHeaders headers = new HttpHeaders();
        PageServer page = getPageServer(request);
        headers.add(HttpHeaders.CONTENT_TYPE, page.getContentType(request));
        return new ResponseEntity<byte[]>(page.getContent(request), headers, HttpStatus.OK);
    }

	public void logUserInfo(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        String uri = request.getRequestURI();
        String query = request.getQueryString();
        String method = request.getMethod();
        String referer = request.getHeader("Referer");
        String language = request.getHeader("Accept-Language");
        String forwarded = request.getHeader("X-Forwarded-For");
        String sessionId = (request.getSession(false) != null) ? request.getSession(false).getId() : "no session";
        String user = (request.getUserPrincipal() != null) ? request.getUserPrincipal().getName() : "anonymous";

        logger.info("""
            Request received:
            - IP: {}
            - Method: {}
            - URI: {}
            - Query: {}
            - User-Agent: {}
            - Referer: {}
            - Accept-Language: {}
            - X-Forwarded-For: {}
            - Session ID: {}
            - Authenticated User: {}
            """, ip, method, uri, query, userAgent, referer, language, forwarded, sessionId, user);
    }

	private PageServer getPageServer(HttpServletRequest request) throws IOException {
		String requestURI = request.getRequestURI();
		PageServer server = getPageServers(request).get(requestURI);

		if (server==null)
			server = getPageServers(request).get(requestURI + (requestURI.charAt(requestURI.length()-1)=='/'?"*":"/*"));
		if (server==null) {
			String uri = requestURI;
			String parentUri = uri.substring(0, uri.length()-1).replaceAll("/[^/]*$", "");

			if (parentUri.isEmpty())
				parentUri = "/";
			server = getPageServers(request).get(parentUri + (parentUri.charAt(parentUri.length()-1)=='/'?"*":"/*"));
		}
		if (server==null)
			throw new IOException("No PageServer defined for URI: " + requestURI);
		return server;
	}
    
    private boolean isSubFolderCatchAll(HttpServletRequest request) {
		String requestURI = request.getRequestURI();
		PageServer server = getPageServers(request).get(requestURI);

		if (server==null)
			return getPageServers(request).get(requestURI + (requestURI.charAt(requestURI.length()-1)=='/'?"*":"/*"))!=null;
		return false;
	}

	private Map<String, PageServer> getPageServers(HttpServletRequest request) {
		return appProperties.getMobileDefaultsToDesktop()&&isMobile(request) ? mobilePageServers : desktopPageServers;
	}
	
	public static boolean isMobile(HttpServletRequest req) {
	    String agent = req.getHeader("User-Agent");
	    if (agent == null) return false;
	    agent = agent.toLowerCase();
	    
	    return agent.contains("mobi")
	   	     || agent.contains("android")
		     || agent.contains("iphone")
		     || agent.contains("ipod")
		     || agent.contains("blackberry")
		     || agent.contains("windows phone");
	}

}
