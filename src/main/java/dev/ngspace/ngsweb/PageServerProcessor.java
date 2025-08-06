package dev.ngspace.ngsweb;

import java.util.Map;

import dev.ngspace.ngsweb.AppProperties.WebStructure;

public class PageServerProcessor {
	private PageServerProcessor() {}
	
	public static PageServer createPageServer(AppProperties properties, WebStructure structure, String key) {
		String servertype = structure.getServertype();
		Map<String, String> custom = structure.getCustom();
		System.out.println(key);
		return switch (servertype) {
			case "html_folder": {
				yield new HTMLFolderPageServer(properties, key, custom.get("headInjectFile"), custom.get("sourcefolder"),
						null);
			}
			case "css_folder": {
				yield new StringFolderPageServer(properties, key, custom.get("sourcefolder"), "text/css");
			}
			case "js_folder": {
				yield new StringFolderPageServer(properties, key, custom.get("sourcefolder"), "text/javascript");
			}
			case "image_folder": {
				yield new ImageFolderPageServer(key, custom.get("sourcefolder"));
			}
			case "raw_folder": {
				yield new RawFolderPageServer(key, custom.get("sourcefolder"));
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
