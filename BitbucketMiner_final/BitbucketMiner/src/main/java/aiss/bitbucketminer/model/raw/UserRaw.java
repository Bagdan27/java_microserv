package aiss.bitbucketminer.model.raw;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRaw {

    @JsonProperty("uuid")
    private String id;

    @JsonProperty("username")
    private String username;

    @JsonProperty("display_name")
    private String name;

    @JsonProperty("links")
    private Links links;

    // Getters & Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Links {

        @JsonProperty("avatar")
        private Avatar avatar;

        @JsonProperty("html")
        private Html html;

        public Avatar getAvatar() {
            return avatar;
        }

        public void setAvatar(Avatar avatar) {
            this.avatar = avatar;
        }

        public Html getHtml() {
            return html;
        }

        public void setHtml(Html html) {
            this.html = html;
        }
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Avatar {

        @JsonProperty("href")
        private String href;

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
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
