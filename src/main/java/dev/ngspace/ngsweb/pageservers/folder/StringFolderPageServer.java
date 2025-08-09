package dev.ngspace.ngsweb.pageservers.folder;

import java.io.IOException;

import dev.ngspace.ngsweb.WebConfig;
import jakarta.servlet.http.HttpServletRequest;

public class StringFolderPageServer extends RawFolderPageServer {

	protected WebConfig properties;
	protected String contenttype;
	
	public StringFolderPageServer(WebConfig properties, String key, String sourcefolder, String contenttype,
			String fallbackpath) {
		super(key, sourcefolder, fallbackpath);
		this.properties = properties;
		this.contenttype = contenttype;
	}
	
	public String processFile(String file) throws IOException {
		return file;
	}
	
	@Override
	public byte[] getContent(HttpServletRequest request) throws IOException {
		return processFile(new String(super.getContent(request))).getBytes();
	}

	@Override
	public String getContentType(HttpServletRequest request) {
		return contenttype;
	}
}
