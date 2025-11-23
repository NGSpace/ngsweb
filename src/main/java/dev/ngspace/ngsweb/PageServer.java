package dev.ngspace.ngsweb;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

public interface PageServer {
	public String getContentType(HttpServletRequest request, String URI);

	public byte[] getContent(HttpServletRequest request, String URI) throws Exception;
	
	public List<String> getSitemapPages(HttpServletRequest request, String key);
}
