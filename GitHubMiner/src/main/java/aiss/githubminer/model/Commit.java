package aiss.githubminer.model;

import aiss.githubminer.model.flat.CommitFlat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Commit {
    @JsonProperty("sha")
    private String id;

    @JsonProperty("commit")
    private CommitDetails commitDetails;

    @JsonProperty("html_url")
    private String webUrl;

    // Nested classes
    public static class CommitDetails {
        @JsonProperty("message") String message;
        @JsonProperty("author") Author author;
    }

    public static class Author {
        @JsonProperty("name") String name;
        @JsonProperty("email") String email;
        @JsonProperty("date") String date;
    }

    public CommitFlat toFlat() {
        String title = commitDetails.message != null
                ? commitDetails.message.split("\n")[0]
                : "";

        String messageBody = commitDetails.message != null && commitDetails.message.split("\n").length > 1
                ? commitDetails.message.split("\n", 2)[1].trim()
                : "";

        return new CommitFlat(
                id,
                title,
                messageBody,
                commitDetails.author != null ? commitDetails.author.name : null,
                commitDetails.author != null ? commitDetails.author.email : null,
                commitDetails.author != null ? commitDetails.author.date : null,
                webUrl
        );
    }
}