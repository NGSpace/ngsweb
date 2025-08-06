package dev.ngspace.ngsweb;

public class HTMLFolderPageServer extends StringFolderPageServer {
	
	protected String headInjectFile;
	
	protected HTMLFolderPageServer(AppProperties properties, String key, String headInjectFile, String sourcefolder) {
		super(properties, key, sourcefolder, "text/html");
		this.headInjectFile = headInjectFile;
	}

	@Override
	public String processFile(String file) {
		return file;
	}
	
	public String ensureTitleTag(String html, String defaultTitle) {
	    if (html == null || defaultTitle == null) return html;

	    // Already has a <title> tag?
	    if (html.toLowerCase().contains("<title>")) {
	    	NGSWebController.logger.info(html);
	        return html;
	    }

	    // Find the <head> tag to inject after
	    int headIndex = html.toLowerCase().indexOf("<head>");
	    if (headIndex == -1) {
	        // No <head> tag at all — leave as is or decide to inject at top
	        return ensureTitleTag("<head></head>"+html,defaultTitle);
	    }

	    int insertPosition = headIndex + "<head>".length();

	    String titleTag = "\n<title>" + defaultTitle + "</title>";

	    // Inject title after <head>
	    return html.substring(0, insertPosition)
	         + titleTag
	         + html.substring(insertPosition);
	}
}
