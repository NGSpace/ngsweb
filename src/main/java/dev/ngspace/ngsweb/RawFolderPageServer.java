package dev.ngspace.ngsweb;

import java.io.File;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.servlet.http.HttpServletRequest;

public class RawFolderPageServer implements PageServer {
	
	private String sourcefolder;
	private String key;
	
	protected RawFolderPageServer(String key, String sourcefolder) {
		this.sourcefolder = sourcefolder;
		this.key = key;
		
	}

	@Override
	public String getContentType(HttpServletRequest request) {
		return "application/octet-stream";//IDK what this means but apparently that's the one for raw data.
	}

	@Override
	public byte[] getContent(HttpServletRequest request) throws Exception {
		
		String uri = request.getRequestURI().replace("%20", " "); // e.g., "/foo/bar/baz"
//		uri = uri.replaceAll("/+$", "");      // remove trailing slashes
//		String[] parts = uri.split("/");
//
//		String lastSegment = parts.length > 0 ? parts[parts.length - 1] : "";
		String nkey = key;
		if (nkey.endsWith("*")) nkey = nkey.substring(0, nkey.length()-1);
		uri = uri.replaceFirst(Pattern.quote(nkey), Matcher.quoteReplacement(""));
		
		if (uri.isBlank()||uri.equals("/")) uri = "index.html";

		System.out.println(uri);
		System.out.println(nkey);
		
		return Files.readAllBytes(new File(sourcefolder + "/" + uri).toPath());
	}
	
}
