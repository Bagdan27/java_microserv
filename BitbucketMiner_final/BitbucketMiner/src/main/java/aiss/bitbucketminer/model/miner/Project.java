package aiss.bitbucketminer.model.miner;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Project {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("web_url")
    private String webUrl;

    @JsonProperty("commits")
    private List<Commit> commits;

    @JsonProperty("issues")
    private List<Issue> issues;

    public Project() {}

    public Project(String id, String name, String webUrl) {
        this.id = id;
        this.name = name;
        this.webUrl = webUrl;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getWebUrl() { return webUrl; }
    public void setWebUrl(String webUrl) { this.webUrl = webUrl; }

    public List<Commit> getCommits() { return commits; }
    public void setCommits(List<Commit> commits) { this.commits = commits; }

    public List<Issue> getIssues() { return issues; }
    public void setIssues(List<Issue> issues) { this.issues = issues; }
}
