package dev.ngspace.ngsweb;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "ngsweb")
public class WebConfig {

	private String defaultTitle;
	private boolean mobileDefaultsToDesktop;
	private Map<String, WebStructure> desktop_webstructure = new HashMap<>();
	private Map<String, WebStructure> mobile_webstructure = new HashMap<>();
	
	
	
	public String getDefaultTitle() {
		return defaultTitle;
	}
	
	public void setDefaultTitle(String defaultTitle) {
		this.defaultTitle = defaultTitle;
	}
	
	
	
	public boolean getMobileDefaultsToDesktop() {
		return mobileDefaultsToDesktop;
	}

	public void setMobileDefaultsToDesktop(boolean mobileDefaultsToDesktop) {
		this.mobileDefaultsToDesktop = mobileDefaultsToDesktop;
	}
	
	
	
	public Map<String, WebStructure> getDesktopWebstructure() {
		return desktop_webstructure;
	}
	
	public void setDesktopWebstructure(Map<String, WebStructure> webstructure) {
		this.desktop_webstructure = webstructure;
	}
	
	
	
	public Map<String, WebStructure> getMobileWebstructure() {
		return mobile_webstructure;
	}
	
	public void setMobileWebstructure(Map<String, WebStructure> webstructure) {
		this.mobile_webstructure = webstructure;
	}
	
	
	
	public static class WebStructure {
		private String servertype;
		private Map<String, String> custom = new HashMap<>();
		
		public String getServertype() {
			return servertype;
		}
		
		public void setServertype(String servertype) {
			this.servertype = servertype;
		}
		
		public Map<String, String> getCustom() {
			return custom;
		}
		
		public void setCustom(Map<String, String> custom) {
			this.custom = custom;
		}
	}
}
