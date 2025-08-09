package dev.ngspace.ngsweb;

import jakarta.servlet.http.HttpServletRequest;

public interface PageServer {
	public String getContentType(HttpServletRequest request);
	
	public byte[] getContent(HttpServletRequest request) throws Exception;
}
