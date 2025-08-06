package dev.ngspace.ngsweb;

import java.io.File;
import java.nio.file.Files;

import jakarta.servlet.http.HttpServletRequest;

public class FilePageServer implements PageServer {
	
	private String file;
	private String contenttype;
	
	protected FilePageServer(String file, String contenttype) {
		this.file = file;
		this.contenttype = contenttype;
		
	}

	@Override
	public String getContentType(HttpServletRequest request) {
		return contenttype;
	}

	@Override
	public byte[] getContent(HttpServletRequest request) throws Exception {
		
		return Files.readAllBytes(new File(file).toPath());
	}
	
}
