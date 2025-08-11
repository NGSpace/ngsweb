package dev.ngspace.ngsweb.pageservers.folder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dev.ngspace.ngsweb.PageServer;
import dev.ngspace.ngsweb.controllers.NGSWebController;
import jakarta.servlet.http.HttpServletRequest;

public class RawFolderPageServer implements PageServer {
	
	protected String[] sources;
	protected String key;
	protected String cleankey;
	protected String fallbackpath;
	
	public RawFolderPageServer(String key, String[] sources, String fallbackpath) {
		this.sources = sources;
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
		
		for (String source : sources) {
			File file = new File(source + "/" + uri);
			if (file.exists())
				return Files.readAllBytes(file.toPath());
		}
		throw new FileNotFoundException("File not found for URI: " + uri);
	}
	
}
