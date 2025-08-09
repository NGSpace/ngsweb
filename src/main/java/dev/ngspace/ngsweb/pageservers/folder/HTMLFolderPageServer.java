package dev.ngspace.ngsweb.pageservers.folder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import dev.ngspace.ngsweb.WebConfig;

public class HTMLFolderPageServer extends StringFolderPageServer {
	
	protected String headInjectFile;
	private String favicon;
	
	public HTMLFolderPageServer(WebConfig properties, String key, String headInjectFile, String sourcefolder,
			String favicon, String fallbackpath) {
		super(properties, key, sourcefolder, "text/html", fallbackpath);
		this.headInjectFile = headInjectFile;
		if (favicon==null) {
			favicon = "/favicon.ico";
		}
		this.favicon = favicon;
	}

	@Override
	public String processFile(String file) throws IOException {
		if (!file.contains("<link rel=\"icon\" href="))
			file = insertToTag(file, "<link rel=\"icon\" href=\""+favicon+"\">", "head");
		if (!file.contains("<title>")&&properties.getDefaultTitle()!=null)
			file = insertToTag(file, "<title>"+properties.getDefaultTitle()+"</title>", "head");
		if (headInjectFile!=null)
			file = insertToTag(file, new String(Files.readAllBytes(new File(headInjectFile).toPath())), "head");
		
		return file;
	}
	
	public String insertToTag(String html, String insertion, String tag) {
	    if (html == null || tag == null) return html;

	    int headIndex = html.toLowerCase().indexOf("<"+tag);
	    if (headIndex == -1)
	        return insertToTag("<"+tag+"> </"+tag+">"+html,insertion, tag);

	    int insertPosition = headIndex + ("<"+tag+">").length();
	    
	    return html.substring(0, insertPosition) + insertion + html.substring(insertPosition);
	}
	
	public String insertDiv(String html, String insertion, String parent, String divsettings) {
	    if (html == null || parent == null) return html;

	    int headIndex = html.toLowerCase().indexOf("<div "+divsettings+">");
	    if (headIndex == -1)
	        return insertDiv(insertToTag(html,"<div "+divsettings+"></div>", parent), insertion, parent, divsettings);

	    int insertPosition = headIndex + ("<div "+divsettings+">").length();
	    
	    return html.substring(0, insertPosition) + insertion + html.substring(insertPosition);
	}
}
