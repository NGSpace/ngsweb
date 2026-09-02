package dev.ngspace.ngsweb.controllers;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import dev.ngspace.ngsweb.WebConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/download/*")
public class DownloadController {
	private Path source = null;
	
	public DownloadController(WebConfig webconf) {
		var srcpath = webconf.getDownloadSource();
		if (srcpath != null)
			this.source = Paths.get(srcpath).toAbsolutePath().normalize();
	}
	
	@GetMapping
	public void download(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (source == null)
			throw new IOException("This instance does not allow downloading files");
		
		String uri = request.getRequestURI();
		
		if (!uri.startsWith("/download/")) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		String requested = uri.substring("/download/".length());
		
		Path file = source.resolve(requested).normalize();
		
		// Ensure the resolved path is still inside the download directory
		if (!file.startsWith(source)) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return;
		}
		
		if (!Files.exists(file) || !Files.isRegularFile(file)) {
			throw new IOException("File not found");
		}
		
		// Get the media type of the file
		String contentType = Files.probeContentType(file);
		if (contentType == null) {
			// Use the default media type
			contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
		}
		
		response.setContentType(contentType);
		// File Size
		response.setContentLengthLong(Files.size(file));
		/**
		 * Building the Content-Disposition header with the ContentDisposition utility
		 * class can avoid the problem of garbled downloaded file names.
		 */
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment()
				.filename(file.getFileName().toString(), StandardCharsets.UTF_8).build().toString());
		// Response data to the client
		Files.copy(file, response.getOutputStream());
	}
}
