package aiss.githubminer.model.flat;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserFlat {
    private String id;
    private String username;
    private String name;
    private String avatarUrl;
    private String webUrl;

    public UserFlat() {}

    public UserFlat(String id, String username, String name, String avatarUrl, String webUrl) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.webUrl = webUrl;
    }

    @JsonProperty("id")
    public String getId() { return id; }
    @JsonProperty("id")
    public void setId(String id) { this.id = id; }

    @JsonProperty("username")
    public String getUsername() { return username; }
    @JsonProperty("username")
    public void setUsername(String username) { this.username = username; }

    @JsonProperty("name")
    public String getName() { return name; }
    @JsonProperty("name")
    public void setName(String name) { this.name = name; }

    @JsonProperty("avatar_url")
    public String getAvatarUrl() { return avatarUrl; }
    @JsonProperty("avatar_url")
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    @JsonProperty("web_url")
    public String getWebUrl() { return webUrl; }
    @JsonProperty("web_url")
    public void setWebUrl(String webUrl) { this.webUrl = webUrl; }
}
