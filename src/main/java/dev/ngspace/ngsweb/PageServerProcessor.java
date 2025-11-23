package dev.ngspace.ngsweb;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import dev.ngspace.ngsweb.WebConfig.WebStructure;
import dev.ngspace.ngsweb.pageservers.FilePageServer;
import dev.ngspace.ngsweb.pageservers.HTMLStringPageServer;
import dev.ngspace.ngsweb.pageservers.NullPageServer;
import dev.ngspace.ngsweb.pageservers.folder.HTMLFolderPageServer;
import dev.ngspace.ngsweb.pageservers.folder.ImageFolderPageServer;
import dev.ngspace.ngsweb.pageservers.folder.RawFolderPageServer;
import dev.ngspace.ngsweb.pageservers.folder.StringFolderPageServer;
import dev.ngspace.ngsweb.pageservers.folder.TemplateHTMLFolderPageServer;

public class PageServerProcessor {
	private PageServerProcessor() {
	}
	
	public static PageServer createPageServer(WebConfig properties, WebStructure structure, String key) {
		String servertype = structure.getServertype();
		Map<String, Object> custom = structure.getCustom();
		return switch (servertype) {
			case "html_folder": {
				yield new HTMLFolderPageServer(properties, key, (String) custom.get("headInjectFile"),
						getSrc(custom), (String) custom.get("favicon"), (String) custom.get("fallbackpath"));
			}
			case "template_folder": {
				yield new TemplateHTMLFolderPageServer(properties, key, (String) custom.get("headInjectFile"),
						getSrc(custom), (String) custom.get("favicon"), (String) custom.get("fallbackpath"),
						(String) custom.get("template"));
			}
			case "css_folder": {
				yield new StringFolderPageServer(properties, key, getSrc(custom), "text/css",
						(String) custom.get("fallbackpath"));
			}
			case "js_folder": {
				yield new StringFolderPageServer(properties, key, getSrc(custom), "text/javascript",
						(String) custom.get("fallbackpath"));
			}
			case "image_folder": {
				yield new ImageFolderPageServer(key, getSrc(custom), (String) custom.get("fallbackpath"));
			}
			case "raw_folder": {
				yield new RawFolderPageServer(key, getSrc(custom), (String) custom.get("fallbackpath"));
			}
			case "raw_file": {
				yield new FilePageServer((String) custom.get("file"), (String) custom.get("contenttype"));
			}
			case "html_string": {
				yield new HTMLStringPageServer((String) custom.get("content"));
			}
			case "null_page": {
				yield new NullPageServer((String) custom.get("contenttype"));
			}
			default:
				throw new IllegalArgumentException("No page-server type: " + servertype);
		};
	}

	private static String[] getSrc(Map<String, Object> custom) {
		Object obj = custom.get("source_folders");
		if (obj instanceof String s)
			return new String[] {s};
		if (obj instanceof List<?> list) {
			String[] arr = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
			    arr[i] = (String) list.get(i);
			}
		}
		if (obj instanceof LinkedHashMap<?, ?> map) {
			var set = map.entrySet();
			String[] arr = new String[map.size()];
			int i = 0;
			for (var o : set) {
				arr[i++] = (String) o.getValue();
			}
			return arr;
		}
		throw new IllegalArgumentException("Custom.source_folders can not be of type: " + obj.getClass());
	}
}
