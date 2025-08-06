package dev.ngspace.ngsweb;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class NGSWebController {
    
	public static final Logger logger = LoggerFactory.getLogger(NGSWebController.class);
	public AppProperties appProperties;
	public Map<String, PageServer> pageservers;

    // Constructor injection (no @Autowired needed on the constructor 
    // if there's only one constructor)
    public NGSWebController(AppProperties appProperties) {
    	this.appProperties = appProperties;
    	pageservers = new HashMap<String, PageServer>();
    	for (var entry : appProperties.getWebstructure().entrySet()) {
    		logger.info("Processing pageserver: " + entry.getKey());
    		pageservers.put(entry.getKey(), PageServerProcessor.createPageServer(appProperties, entry.getValue(), entry.getKey()));
    	}
    }
    
	@GetMapping("/error")
    public ResponseEntity<String> erorrPlace(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "text/html");
        return new ResponseEntity<String>("How badly did you fuck up to reach this page?",
        		headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }
	
	@GetMapping("/**")
    public ResponseEntity<byte[]> customContentType(HttpServletRequest request) throws Exception {
    	
    	logUserInfo(request);
    	
    	if (request.getRequestURI().contains("..")) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, "text/html");
            return new ResponseEntity<byte[]>("Fuck off bitch.".getBytes(),
            		headers, HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    	
        HttpHeaders headers = new HttpHeaders();
        PageServer page = getPageServer(request.getRequestURI());
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

	private PageServer getPageServer(String requestURI) {
		PageServer server = pageservers.get(requestURI);
		if (server==null) {
			String uri = requestURI; // e.g., "/foo/bar/baz"
			String parentUri = uri.replaceAll("/[^/]*$", ""); // strips last segment

			// Special case: if root "/"
			if (parentUri.isEmpty()) {
			    parentUri = "/";
			}
			logger.info(parentUri);
			server = pageservers.get(parentUri + (parentUri.charAt(parentUri.length()-1)=='/'?"*":"/*"));
		}
		return server;
	}
}
