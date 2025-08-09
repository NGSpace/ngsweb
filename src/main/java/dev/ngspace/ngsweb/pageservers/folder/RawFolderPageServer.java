package dev.ngspace.ngsweb.pageservers.folder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dev.ngspace.ngsweb.PageServer;
import jakarta.servlet.http.HttpServletRequest;

public class RawFolderPageServer implements PageServer {
	
	protected String sourcefolder;
	protected String key;
	protected String cleankey;
	protected String fallbackpath;
	
	public RawFolderPageServer(String key, String sourcefolder, String fallbackpath) {
		this.sourcefolder = sourcefolder;
		this.fallbackpath = fallbackpath == null ? "index.html" : "";
		this.key = key;
		this.cleankey = key;
		if (cleankey.endsWith("/*")) cleankey = cleankey.substring(0, cleankey.length()-2);
	}

	@Override
	public String getContentType(HttpServletRequest request) {
		return "application/octet-stream";//IDK what this means but apparently that's the one for raw data.
	}
	
	public String getDefaultFile() {
		return fallbackpath;
	}

	@Override
	public byte[] getContent(HttpServletRequest request) throws IOException {
		String uri = request.getRequestURI().replace("%20", " ");
		uri = uri.replaceFirst(Pattern.quote(cleankey), Matcher.quoteReplacement(""));
		if (uri.isBlank()||uri.equals("/")) uri = fallbackpath;
		
		return Files.readAllBytes(new File(sourcefolder + "/" + uri).toPath());
	}
	
}
