package dev.ngspace.ngsweb.pageservers;

import java.util.List;

import dev.ngspace.ngsweb.PageServer;
import jakarta.servlet.http.HttpServletRequest;

public class HTMLStringPageServer implements PageServer {

	private String content;

	public HTMLStringPageServer(String content) {
		this.content = content;
	}

	@Override
	public String getContentType(HttpServletRequest request, String URI) {
		return "text/html";
	}

	@Override
	public byte[] getContent(HttpServletRequest request, String URI) throws Exception {
		return content.getBytes();
	}

	@Override
	public List<String> getPages(HttpServletRequest request, String key) {
		return List.of(key);
	}
}
