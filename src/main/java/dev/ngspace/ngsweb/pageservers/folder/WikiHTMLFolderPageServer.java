package dev.ngspace.ngsweb.pageservers.folder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import dev.ngspace.ngsweb.WebConfig;

public class WikiHTMLFolderPageServer extends HTMLFolderPageServer {

	protected String baseFile;

	public WikiHTMLFolderPageServer(WebConfig properties, String key, String headInjectFile, String[] sources,
			String favicon, String fallbackpath, String baseFile) {
		super(properties, key, headInjectFile, sources, favicon, fallbackpath);
		this.baseFile = baseFile;
	}
	
	@Override
	public String processFile(String contents) throws IOException {
		String baseHTML = Files.readString(Path.of(baseFile));
		String title = getTitle(contents);
		contents = removeTitle(contents);
		baseHTML = insertToTag(baseHTML, "<title>"+title+"</title>", "head");
		contents = insertDiv(baseHTML, contents, "body", "id=\"contents\"");
		
		return super.processFile(contents);
	}

	public String getTitle(String html) {
	    int titleIndex = html.toLowerCase().indexOf("<title>");
	    if (titleIndex == -1)
	        return properties.getDefaultTitle();

	    int titlePos = titleIndex + "<title>".length();
	    
	    return html.substring(titlePos, html.indexOf('<', titlePos));
	}

	public String removeTitle(String html) {
	    int titleIndex = html.toLowerCase().indexOf("<title>");
	    if (titleIndex == -1)
	        return html;

	    int titlePos = titleIndex;
	    
	    return html.substring(0, titlePos) + html.substring(html.indexOf('>', titlePos + "<title>".length())+1);
	}
}
