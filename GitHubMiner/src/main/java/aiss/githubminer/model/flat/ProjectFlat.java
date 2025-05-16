package aiss.githubminer.model.flat;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class ProjectFlat {
    private String id;
    private String name;
    private String webUrl;
    private List<CommitFlat> commits;
    private List<IssueFlat> issues;

    public ProjectFlat() {}

    public ProjectFlat(String id, String name, String webUrl,
                       List<CommitFlat> commits, List<IssueFlat> issues) {
        this.id = id;
        this.name = name;
        this.webUrl = webUrl;
        this.commits = commits;
        this.issues = issues;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("full_name")
    public String getName() {
        return name;
    }

    @JsonProperty("full_name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("html_url")
    public String getWebUrl() {
        return webUrl;
    }

    @JsonProperty("html_url")
    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    @JsonProperty("commits")
    public List<CommitFlat> getCommits() {
        return commits;
    }

    @JsonProperty("commits")
    public void setCommits(List<CommitFlat> commits) {
        this.commits = commits;
    }

    @JsonProperty("issues")
    public List<IssueFlat> getIssues() {
        return issues;
    }

    @JsonProperty("issues")
    public void setIssues(List<IssueFlat> issues) {
        this.issues = issues;
    }
}
