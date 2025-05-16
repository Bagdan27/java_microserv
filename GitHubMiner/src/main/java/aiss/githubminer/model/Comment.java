package aiss.githubminer.model;

import aiss.githubminer.model.flat.CommentFlat;
import aiss.githubminer.model.flat.UserFlat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Comment {

        @JsonProperty("id")
        private String id;

        @JsonProperty("body")
        private String body;

        @JsonProperty("created_at")
        private String createdAt;

        @JsonProperty("updated_at")
        private String updatedAt;

        @JsonProperty("user")
        private User author;

        @JsonProperty("html_url")
        private String webUrl;

        @JsonIgnore
        private Issue issue;

        // Getters and setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getBody() { return body; }
        public void setBody(String body) { this.body = body; }

        public String getCreatedAt() { return createdAt; }
        public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

        public String getUpdatedAt() { return updatedAt; }
        public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

        public User getAuthor() { return author; }
        public void setAuthor(User author) { this.author = author; }

        public String getWebUrl() { return webUrl; }
        public void setWebUrl(String webUrl) { this.webUrl = webUrl; }

        @JsonIgnore
        public Issue getIssue() { return issue; }
        public void setIssue(Issue issue) { this.issue = issue; }

        // Flatten to DTO
        public CommentFlat toFlat() {
                UserFlat authorFlat = author != null ? author.toFlat() : null;
                return new CommentFlat(
                        id,
                        body,
                        createdAt,
                        updatedAt,
                        authorFlat,
                        webUrl
                );
        }

        @Override
        public boolean equals(Object o) {
                if (this == o) return true;
                if (!(o instanceof Comment)) return false;
                Comment comment = (Comment) o;
                return Objects.equals(id, comment.id);
        }

        @Override
        public int hashCode() {
                return Objects.hash(id);
        }
}
