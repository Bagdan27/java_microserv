package aiss.githubminer.model;

import aiss.githubminer.model.flat.CommentFlat;
import aiss.githubminer.model.flat.IssueFlat;
import aiss.githubminer.model.flat.UserFlat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Issue {
    @JsonProperty("id")
    private String id;

    @JsonProperty("number")
    private int number;

    @JsonProperty("title")
    private String title;

    @JsonProperty("body")
    private String description;

    @JsonProperty("state")
    private String state;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("updated_at")
    private String updatedAt;

    @JsonProperty("closed_at")
    private String closedAt;

    @JsonProperty("reactions")
    private Reactions reactions;

    @JsonProperty("labels")
    private List<Label> labelTemp;

    @JsonProperty("user")
    private User author;

    @JsonProperty("assignee")
    private User assignee;

    @JsonIgnore
    private final List<Comment> comments = new ArrayList<>();

    // Setters
    public void setId(String id) { this.id = id; }
    public void setNumber(int number) { this.number = number; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setState(String state) { this.state = state; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
    public void setClosedAt(String closedAt) { this.closedAt = closedAt; }
    public void setReactions(Reactions reactions) { this.reactions = reactions; }
    public void setLabelTemp(List<Label> labelTemp) { this.labelTemp = labelTemp; }
    public void setAuthor(User author) { this.author = author; }
    public void setAssignee(User assignee) { this.assignee = assignee; }

    // Getters
    public String getId() { return id; }
    public int getNumber() { return number; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getState() { return state; }
    public String getCreatedAt() { return createdAt; }
    public String getUpdatedAt() { return updatedAt; }
    public String getClosedAt() { return closedAt; }
    public Integer getVotes() { return reactions != null ? reactions.getTotalVotes() : 0; }
    public User getAuthor() { return author; }
    public User getAssignee() { return assignee; }

    // Label processing
    public List<String> getLabels() {
        return labelTemp == null ?
                Collections.emptyList() :
                labelTemp.stream().map(Label::getName).toList();
    }

    // Comments handling
    public List<Comment> getComments() {
        return Collections.unmodifiableList(comments);
    }

    public void addComment(Comment comment) {
        if (comment != null && !comments.contains(comment)) {
            comments.add(comment);
            if (comment.getIssue() != this) {
                comment.setIssue(this);
            }
        }
    }

    void internalRemoveComment(Comment comment) {
        comments.remove(comment);
    }

    public IssueFlat toFlat() {
        List<CommentFlat> commentFlats = comments.stream()
                .map(c -> {
                    UserFlat commentAuthor = c.getAuthor() != null ? c.getAuthor().toFlat() : null;
                    return new CommentFlat(
                            c.getId(),
                            c.getBody(),
                            c.getCreatedAt(),
                            c.getUpdatedAt(),
                            commentAuthor,
                            c.getWebUrl()
                    );
                })
                .toList();

        UserFlat authorFlat = author != null ? author.toFlat() : null;
        UserFlat assigneeFlat = assignee != null ? assignee.toFlat() : null;

        IssueFlat flat = new IssueFlat(
                id,
                title,
                description,
                state,
                createdAt,
                updatedAt,
                closedAt,
                getLabels(),
                getVotes(),
                authorFlat,
                assigneeFlat,
                commentFlats
        );
        flat.setNumber(number);
        return flat;
    }

    // Nested classes
    public static class Label {
        @JsonProperty("name") private String name;
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    public static class Reactions {
        @JsonProperty("+1") private int upvotes;
        @JsonProperty("-1") private int downvotes;
        public int getTotalVotes() { return upvotes - downvotes; }
        public void setUpvotes(int upvotes) { this.upvotes = upvotes; }
        public void setDownvotes(int downvotes) { this.downvotes = downvotes; }
    }
}
