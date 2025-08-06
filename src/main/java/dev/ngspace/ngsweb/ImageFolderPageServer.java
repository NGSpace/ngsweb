package dev.ngspace.ngsweb;

import jakarta.servlet.http.HttpServletRequest;

public class ImageFolderPageServer extends RawFolderPageServer {

	protected ImageFolderPageServer(String key, String sourcefolder) {
		super(key, sourcefolder);
	}
	
	@Override
	public String getContentType(HttpServletRequest request) {
		String filename = request.getRequestURI();
		return switch (filename.substring(filename.lastIndexOf('.') + 1)) {
			case "jpg","jpeg" -> "image/jpeg";
			case "webp" -> "image/webp";
			case "gif" -> "image/gif";
			case "bmp" -> "image/bmp";
			case "avif" -> "image/avif";
			case "apng" -> "image/apng";
			case "svg" -> "image/svg+xml";
			case "tiff" -> "image/tiff";
			default -> "image/png";// When in doubt, it's probably a png!
		};
	}
}
