package dev.ngspace.ngsweb;

import java.util.Map;

import dev.ngspace.ngsweb.WebConfig.WebStructure;
import dev.ngspace.ngsweb.pageservers.FilePageServer;
import dev.ngspace.ngsweb.pageservers.HTMLStringPageServer;
import dev.ngspace.ngsweb.pageservers.folder.HTMLFolderPageServer;
import dev.ngspace.ngsweb.pageservers.folder.ImageFolderPageServer;
import dev.ngspace.ngsweb.pageservers.folder.RawFolderPageServer;
import dev.ngspace.ngsweb.pageservers.folder.StringFolderPageServer;
import dev.ngspace.ngsweb.pageservers.folder.WikiHTMLFolderPageServer;

public class PageServerProcessor {
	private PageServerProcessor() {
	}
	
	public static PageServer createPageServer(WebConfig properties, WebStructure structure, String key) {
		String servertype = structure.getServertype();
		Map<String, String> custom = structure.getCustom();
		return switch (servertype) {
			case "html_folder": {
				yield new HTMLFolderPageServer(properties, key, custom.get("headInjectFile"),
						custom.get("sourcefolder"), custom.get("favicon"), custom.get("fallbackpath"));
			}
			case "wiki_folder": {
				yield new WikiHTMLFolderPageServer(properties, key, custom.get("headInjectFile"),
						custom.get("sourcefolder"), custom.get("favicon"), custom.get("fallbackpath"),
						custom.get("base_file"));
			}
			case "css_folder": {
				yield new StringFolderPageServer(properties, key, custom.get("sourcefolder"), "text/css",
						custom.get("fallbackpath"));
			}
			case "js_folder": {
				yield new StringFolderPageServer(properties, key, custom.get("sourcefolder"), "text/javascript",
						custom.get("fallbackpath"));
			}
			case "image_folder": {
				yield new ImageFolderPageServer(key, custom.get("sourcefolder"), custom.get("fallbackpath"));
			}
			case "raw_folder": {
				yield new RawFolderPageServer(key, custom.get("sourcefolder"), custom.get("fallbackpath"));
			}
			case "raw_file": {
				yield new FilePageServer(custom.get("file"), custom.get("contenttype"));
			}
			case "html_string": {
				yield new HTMLStringPageServer(custom.get("content"));
			}
			default:
				throw new IllegalArgumentException("Unexpected value: " + servertype);
		};
	}
}
