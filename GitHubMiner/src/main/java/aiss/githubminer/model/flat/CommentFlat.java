package aiss.githubminer.model.flat;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CommentFlat {

    private String id;
    private String body;
    private String createdAt;
    private String updatedAt;
    private UserFlat author; // full object expected by GitMiner
    private String webUrl;

    public CommentFlat() {}

    public CommentFlat(String id, String body, String createdAt, String updatedAt,
                       UserFlat author, String webUrl) {
        this.id = id;
        this.body = body;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.author = author;
        this.webUrl = webUrl;
    }

    @JsonProperty("id")
    public String getId() { return id; }
    @JsonProperty("id")
    public void setId(String id) { this.id = id; }

    @JsonProperty("body")
    public String getBody() { return body; }
    @JsonProperty("body")
    public void setBody(String body) { this.body = body; }

    @JsonProperty("created_at")
    public String getCreatedAt() { return createdAt; }
    @JsonProperty("created_at")
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    @JsonProperty("updated_at")
    public String getUpdatedAt() { return updatedAt; }
    @JsonProperty("updated_at")
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

    @JsonProperty("author")
    public UserFlat getAuthor() { return author; }
    @JsonProperty("author")
    public void setAuthor(UserFlat author) { this.author = author; }

    @JsonProperty("web_url")
    public String getWebUrl() { return webUrl; }
    @JsonProperty("web_url")
    public void setWebUrl(String webUrl) { this.webUrl = webUrl; }
}
