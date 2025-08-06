package dev.ngspace.ngsweb;

import jakarta.servlet.http.HttpServletRequest;

public class HTMLStringPageServer implements PageServer {

	private String content;

	public HTMLStringPageServer(String content) {
		this.content = content;
	}

	@Override
	public String getContentType(HttpServletRequest request) {
		return "text/html";
	}

	@Override
	public byte[] getContent(HttpServletRequest request) throws Exception {
		return content.getBytes();
	}
}
