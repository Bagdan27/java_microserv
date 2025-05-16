package aiss.githubminer.model;

import aiss.githubminer.model.flat.UserFlat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    @JsonProperty("id") private String id;
    @JsonProperty("login") private String username;
    @JsonProperty("name") private String name;
    @JsonProperty("avatar_url") private String avatarUrl;
    @JsonProperty("html_url") private String webUrl;

    // Relationship stuff 0 init
    @JsonIgnore private final List<Issue> authoredIssues = new ArrayList<>();
    @JsonIgnore private final List<Issue> assignedIssues = new ArrayList<>();
    @JsonIgnore private final List<Comment> authoredComments = new ArrayList<>();

    // Constructor
    public User(@JsonProperty("id") String id,
                @JsonProperty("login") String username,
                @JsonProperty("name") String name,
                @JsonProperty("avatar_url") String avatarUrl,
                @JsonProperty("html_url") String webUrl) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.webUrl = webUrl;
    }

    // Getters
    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getName() { return name; }
    public String getAvatarUrl() { return avatarUrl; }
    public String getWebUrl() { return webUrl; }

    // relationship stuff 1
    public List<Issue> getAuthoredIssues() {
        return Collections.unmodifiableList(authoredIssues);
    }

    public List<Issue> getAssignedIssues() {
        return Collections.unmodifiableList(assignedIssues);
    }

    public List<Comment> getAuthoredComments() {
        return Collections.unmodifiableList(authoredComments);
    }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setName(String name) { this.name = name; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    public void setWebUrl(String webUrl) { this.webUrl = webUrl; }

    // Relationship stuff 2
    public void addAuthoredIssue(Issue issue) {
        if (issue != null && !authoredIssues.contains(issue)) {
            authoredIssues.add(issue);
            if (issue.getAuthor() != this) {
                issue.setAuthor(this); // Bidirectional sync
            }
        }
    }

    public void addAssignedIssue(Issue issue) {
        if (issue != null && !assignedIssues.contains(issue)) {
            assignedIssues.add(issue);
            if (issue.getAssignee() != this) {
                issue.setAssignee(this); // Bidirectional sync
            }
        }
    }

    public void addAuthoredComment(Comment comment) {
        if (comment != null && !authoredComments.contains(comment)) {
            authoredComments.add(comment);
            if (comment.getAuthor() != this) {
                comment.setAuthor(this); // Bidirectional sync
            }
        }
    }

    //removal
    void internalRemoveAuthoredIssue(Issue issue) {
        authoredIssues.remove(issue);
    }

    void internalRemoveAssignedIssue(Issue issue) {
        assignedIssues.remove(issue);
    }

    void internalRemoveAuthoredComment(Comment comment) {
        authoredComments.remove(comment);
    }

    public UserFlat toFlat() {
        return new UserFlat(
                id,
                username,
                name,
                avatarUrl,
                webUrl
        );
    }

}