package dev.ngspace.ngsweb;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "ngsweb")
public class AppProperties {

    private String defaultTitle;
    private Map<String, WebStructure> webstructure = new HashMap<>();

    public String getDefaultTitle() {
        return defaultTitle;
    }

    public void setDefaultTitle(String defaultTitle) {
        this.defaultTitle = defaultTitle;
    }

    public Map<String, WebStructure> getWebstructure() {
        return webstructure;
    }

    public void setWebstructure(Map<String, WebStructure> webstructure) {
        this.webstructure = webstructure;
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
