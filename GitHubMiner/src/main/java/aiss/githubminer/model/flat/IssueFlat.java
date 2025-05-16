package aiss.githubminer.model.flat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

public class IssueFlat {
    private String id;
    private int number;
    private String title;
    private String description;
    private String state;
    private String createdAt;
    private String updatedAt;
    private String closedAt;
    private List<String> labels;
    private Integer votes;
    private UserFlat author;
    private UserFlat assignee;
    private List<CommentFlat> comments;

    public IssueFlat() {}

    public IssueFlat(String id, String title, String description, String state,
                     String createdAt, String updatedAt, String closedAt,
                     List<String> labels, Integer votes,
                     UserFlat author, UserFlat assignee,
                     List<CommentFlat> comments) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.state = state;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.closedAt = closedAt;
        this.labels = labels;
        this.votes = votes;
        this.author = author;
        this.assignee = assignee;
        this.comments = comments;
    }

    @JsonProperty("number")
    public void setNumber(int number) { this.number = number; }

    @JsonIgnore
    public int getNumber() { return number; }

    @JsonProperty("id")
    public String getId() { return id; }
    @JsonProperty("id")
    public void setId(String id) { this.id = id; }

    @JsonProperty("title")
    public String getTitle() { return title; }
    @JsonProperty("title")
    public void setTitle(String title) { this.title = title; }

    @JsonProperty("description")
    public String getDescription() { return description; }
    @JsonProperty("description")
    public void setDescription(String description) { this.description = description; }

    @JsonProperty("state")
    public String getState() { return state; }
    @JsonProperty("state")
    public void setState(String state) { this.state = state; }

    @JsonProperty("created_at")
    public String getCreatedAt() { return createdAt; }
    @JsonProperty("created_at")
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    @JsonProperty("updated_at")
    public String getUpdatedAt() { return updatedAt; }
    @JsonProperty("updated_at")
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

    @JsonProperty("closed_at")
    public String getClosedAt() { return closedAt; }
    @JsonProperty("closed_at")
    public void setClosedAt(String closedAt) { this.closedAt = closedAt; }

    @JsonProperty("labels")
    public List<String> getLabels() { return labels; }
    @JsonProperty("labels")
    public void setLabels(List<String> labels) { this.labels = labels; }

    @JsonProperty("votes")
    public Integer getVotes() { return votes; }
    @JsonProperty("votes")
    public void setVotes(Integer votes) { this.votes = votes; }

    @JsonProperty("author")
    public UserFlat getAuthor() { return author; }
    @JsonProperty("author")
    public void setAuthor(UserFlat author) { this.author = author; }

    @JsonProperty("assignee")
    public UserFlat getAssignee() { return assignee; }
    @JsonProperty("assignee")
    public void setAssignee(UserFlat assignee) { this.assignee = assignee; }

    @JsonProperty("comments")
    public List<CommentFlat> getComments() { return comments; }
    @JsonProperty("comments")
    public void setComments(List<CommentFlat> comments) { this.comments = comments; }
}
