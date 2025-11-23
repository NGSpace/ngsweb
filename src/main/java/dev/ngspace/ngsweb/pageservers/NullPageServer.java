package dev.ngspace.ngsweb.pageservers;

import java.util.List;

import dev.ngspace.ngsweb.PageServer;
import jakarta.servlet.http.HttpServletRequest;

public class NullPageServer implements PageServer {

	private String type;

	public NullPageServer(String type) {
		this.type = type;
	}

	@Override
	public String getContentType(HttpServletRequest request, String URI) {
		return type;
	}

	@Override
	public byte[] getContent(HttpServletRequest request, String URI) throws Exception {
		return new byte[0];
	}

	@Override
	public List<String> getSitemapPages(HttpServletRequest request, String key) {
		return List.of(key);
	}
}
