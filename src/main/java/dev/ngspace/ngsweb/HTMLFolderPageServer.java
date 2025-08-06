package dev.ngspace.ngsweb;

public class HTMLFolderPageServer extends StringFolderPageServer {
	
	protected String headInjectFile;
	private String favicon;
	
	protected HTMLFolderPageServer(AppProperties properties, String key, String headInjectFile, String sourcefolder,
			String favicon) {
		super(properties, key, sourcefolder, "text/html");
		this.headInjectFile = headInjectFile;
		if (favicon==null) {
			favicon = cleankey + "/favicon.ico";
		}
		this.favicon = favicon;
	}

	@Override
	public String processFile(String file) {
//		file = ensureTag(file, "")
		if (!file.contains("<link rel=\"icon\" href=")) {
			file = insertToHead(file, "<link rel=\"icon\" href=\""+favicon+"\">");
		}
		
		return file;
	}
	
	public String insertToHead(String html, String tag) {
	    if (html == null || tag == null) return html;

	    // Find the <head> tag to inject after
	    int headIndex = html.toLowerCase().indexOf("<head>");
	    if (headIndex == -1) {
	        // No <head> tag at all — leave as is or decide to inject at top
	        return insertToHead("<head></head>"+html,tag);
	    }

	    int insertPosition = headIndex + "<head>".length();

	    String titleTag = tag;

	    // Inject title after <head>
	    return html.substring(0, insertPosition)
	         + titleTag
	         + html.substring(insertPosition);
	}
}
