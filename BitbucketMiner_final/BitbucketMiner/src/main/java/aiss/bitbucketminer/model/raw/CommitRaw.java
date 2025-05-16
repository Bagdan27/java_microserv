package aiss.bitbucketminer.model.raw;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CommitRaw {

    @JsonProperty("hash")
    private String hash;

    @JsonProperty("message")
    private String message;

    @JsonProperty("date")
    private String date;

    @JsonProperty("author")
    private Author author;

    @JsonProperty("links")
    private Links links;

    @JsonProperty("summary")
    private Summary summary;

    // Getters & Setters

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public Summary getSummary() {
        return summary;
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Author {

        @JsonProperty("raw")
        private String raw;

        @JsonProperty("user")
        private User user;

        public String getRaw() {
            return raw;
        }

        public void setRaw(String raw) {
            this.raw = raw;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public String getRawEmail() {
            if (raw == null) return null;
            int start = raw.indexOf('<');
            int end = raw.indexOf('>');
            if (start != -1 && end != -1 && end > start) {
                return raw.substring(start + 1, end);
            }
            return null;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class User {
        @JsonProperty("display_name")
        private String displayName;

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
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

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Summary {
        @JsonProperty("raw")
        private String raw;

        public String getRaw() {
            return raw;
        }

        public void setRaw(String raw) {
            this.raw = raw;
        }
    }
}
