package aiss.bitbucketminer.model.raw;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IssueRaw {

    @JsonProperty("id")
    private String id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("content")
    private Content content;

    @JsonProperty("state")
    private String state;

    @JsonProperty("created_on")
    private String createdOn;

    @JsonProperty("updated_on")
    private String updatedOn;

    @JsonProperty("closed_on")
    private String closedOn;

    @JsonProperty("kind")
    private String kind;

    @JsonProperty("priority")
    private String priority;

    @JsonProperty("labels")
    private List<String> labels;

    @JsonProperty("votes")
    private Integer votes;

    @JsonProperty("reporter")
    private UserRaw reporter;

    @JsonProperty("assignee")
    private UserRaw assignee;

    @JsonProperty("links")
    private Links links;

    // Getters & Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getClosedOn() {
        return closedOn;
    }

    public void setClosedOn(String closedOn) {
        this.closedOn = closedOn;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }

    public UserRaw getReporter() {
        return reporter;
    }

    public void setReporter(UserRaw reporter) {
        this.reporter = reporter;
    }

    public UserRaw getAssignee() {
        return assignee;
    }

    public void setAssignee(UserRaw assignee) {
        this.assignee = assignee;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Content {
        @JsonProperty("raw")
        private String raw;

        public String getRaw() {
            return raw;
        }

        public void setRaw(String raw) {
            this.raw = raw;
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
