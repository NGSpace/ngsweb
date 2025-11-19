package dev.ngspace.ngsweb.pageservers;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

import dev.ngspace.ngsweb.PageServer;
import jakarta.servlet.http.HttpServletRequest;

public class FilePageServer implements PageServer {
	
	protected String file;
	protected String contenttype;
	
	public FilePageServer(String file, String contenttype) {
		this.file = file;
		this.contenttype = contenttype;
		
	}

	@Override
	public String getContentType(HttpServletRequest request, String URI) {
		return contenttype;
	}

	@Override
	public byte[] getContent(HttpServletRequest request, String URI) throws Exception {
		return Files.readAllBytes(new File(file).toPath());
	}

	@Override
	public List<String> getPages(HttpServletRequest request, String key) {
		return List.of(key);
	}
	
}
