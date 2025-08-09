package dev.ngspace.ngsweb.pageservers;

import java.io.File;
import java.nio.file.Files;

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
	public String getContentType(HttpServletRequest request) {
		return contenttype;
	}

	@Override
	public byte[] getContent(HttpServletRequest request) throws Exception {
		return Files.readAllBytes(new File(file).toPath());
	}
	
}
