package aiss.bitbucketminer.model.raw;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectRaw {

    @JsonProperty("project")
    private InnerProject project;

    public InnerProject getProject() {
        return project;
    }

    public void setProject(InnerProject project) {
        this.project = project;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class InnerProject {
        @JsonProperty("uuid")
        private String uuid;

        @JsonProperty("name")
        private String name;

        @JsonProperty("links")
        private Links links;

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Links getLinks() {
            return links;
        }

        public void setLinks(Links links) {
            this.links = links;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Links {
        @JsonProperty("html")
        private Html html;

        public Html getHtml() {
            return html;
        }

        public void setHtml(Html html) {
            this.html = html;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Html {
        @JsonProperty("href")
        private String href;

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }
    }
}
