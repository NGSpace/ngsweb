package dev.ngspace.ngsweb;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.servlet.http.HttpServletRequest;

public class StringFolderPageServer implements PageServer {

	protected AppProperties properties;
	protected String sourcefolder;
	protected String contenttype;
	protected String key;
	protected String cleankey;
	
	protected StringFolderPageServer(AppProperties properties, String key, String sourcefolder, String contenttype) {
		this.properties = properties;
		this.key = key;
		this.sourcefolder = sourcefolder;
		this.contenttype = contenttype;
		this.cleankey = key;
		if (cleankey.endsWith("*")) cleankey = cleankey.substring(0, cleankey.length()-1);
		System.out.println(key);
	}
	
	public String processFile(String file) {
		return file;
	}
	
	@Override
	public byte[] getContent(HttpServletRequest request) throws Exception {
		System.out.println(key);
		if (request.getRequestURI().contains("hud")) {
			NGSWebController.logger.info("Hudddddddd");
			NGSWebController.logger.info(key);
		}
		
		String uri = request.getRequestURI().replace("%20", " "); // e.g., "/foo/bar/baz"
//		uri = uri.replaceAll("/+$", "");      // remove trailing slashes
//		String[] parts = uri.split("/");
//
//		String lastSegment = parts.length > 0 ? parts[parts.length - 1] : "";
		uri = uri.replaceFirst(Pattern.quote(cleankey), Matcher.quoteReplacement(""));
		
		if (uri.isBlank()||uri.equals("/")) uri = "index.html";
		
		return processFile(readFile(new File(sourcefolder + "/" + uri))).getBytes();
	}
	
	public String readFile(File file) throws IOException {
		Scanner reader = new Scanner(file);
		String res = "";
		while (reader.hasNextLine()) {
			res += reader.nextLine();
			res += '\n';
		}
		reader.close();
		if (res.isEmpty())
			return res;
		return res.substring(0, res.length() - 1);
		
	}

	@Override
	public String getContentType(HttpServletRequest request) {
		return contenttype;
	}
}
