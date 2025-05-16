package aiss.githubminer.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Project {

    @JsonProperty("id")
    private String id;

    @JsonProperty("full_name")
    private String name;

    @JsonProperty("html_url")
    private String webUrl;

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getWebUrl() {
        return webUrl;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }
}
