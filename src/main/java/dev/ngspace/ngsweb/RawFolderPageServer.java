package dev.ngspace.ngsweb;

import java.io.File;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.servlet.http.HttpServletRequest;

public class RawFolderPageServer implements PageServer {
	
	protected String sourcefolder;
	protected String key;
	protected String cleankey;
	
	protected RawFolderPageServer(String key, String sourcefolder) {
		this.sourcefolder = sourcefolder;
		this.key = key;
		this.cleankey = key;
		if (cleankey.endsWith("/*")) cleankey = cleankey.substring(0, cleankey.length()-2);
	}

	@Override
	public String getContentType(HttpServletRequest request) {
		return "application/octet-stream";//IDK what this means but apparently that's the one for raw data.
	}

	@Override
	public byte[] getContent(HttpServletRequest request) throws Exception {
		
		String uri = request.getRequestURI().replace("%20", " ");
		if (uri.equals(cleankey)||uri.isBlank()||uri.equals("/")) uri = "/index.html";
		else uri = uri.replaceFirst(Pattern.quote(cleankey), Matcher.quoteReplacement(""));
		
		System.out.println("RAw:"+sourcefolder);
		
		return Files.readAllBytes(new File(sourcefolder + "/" + uri).toPath());
	}
	
}
