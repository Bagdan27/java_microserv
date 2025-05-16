package aiss.bitbucketminer.model.miner;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Commit {

    @JsonProperty("id")
    private String id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("message")
    private String message;

    @JsonProperty("author_name")
    private String authorName;

    @JsonProperty("author_email")
    private String authorEmail;

    @JsonProperty("authored_date")
    private String authoredDate;

    @JsonProperty("web_url")
    private String webUrl;

    private Project project;

    public Commit() {}

    public Commit(String id, String title, String message, String authorName,
                  String authorEmail, String authoredDate, String webUrl) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.authorName = authorName;
        this.authorEmail = authorEmail;
        this.authoredDate = authoredDate;
        this.webUrl = webUrl;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }

    public String getAuthorEmail() { return authorEmail; }
    public void setAuthorEmail(String authorEmail) { this.authorEmail = authorEmail; }

    public String getAuthoredDate() { return authoredDate; }
    public void setAuthoredDate(String authoredDate) { this.authoredDate = authoredDate; }

    public String getWebUrl() { return webUrl; }
    public void setWebUrl(String webUrl) { this.webUrl = webUrl; }

    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }
}
