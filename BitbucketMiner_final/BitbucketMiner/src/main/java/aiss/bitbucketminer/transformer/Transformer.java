package aiss.bitbucketminer.transformer;

import aiss.bitbucketminer.model.miner.*;
import aiss.bitbucketminer.model.raw.*;

import java.time.Instant;
import java.util.List;

public class Transformer {

    public static User toUser(UserRaw raw) {
        if (raw == null) return null;
        return new User(
                raw.getId(),
                raw.getUsername(),
                raw.getName(),
                raw.getLinks() != null && raw.getLinks().getAvatar() != null
                        ? raw.getLinks().getAvatar().getHref()
                        : null,
                raw.getLinks() != null && raw.getLinks().getHtml() != null
                        ? raw.getLinks().getHtml().getHref()
                        : null
        );
    }

    public static Comment toComment(CommentRaw raw) {
        if (raw == null) return null;
        return new Comment(
                raw.getId(),
                raw.getContent() != null ? raw.getContent().getRaw() : null,
                formatInstantToLocalDateTimeString(parseInstant(raw.getCreatedOn())),
                formatInstantToLocalDateTimeString(parseInstant(raw.getUpdatedOn())),
                toUser(raw.getUser())
        );
    }

    public static Issue toIssue(IssueRaw raw, List<Comment> comments) {
        if (raw == null) return null;
        return new Issue(
                raw.getId(),
                raw.getTitle(),
                raw.getContent() != null ? raw.getContent().getRaw() : null,
                raw.getState(),
                formatInstantToLocalDateTimeString(parseInstant(raw.getCreatedOn())),
                formatInstantToLocalDateTimeString(parseInstant(raw.getUpdatedOn())),
                formatInstantToLocalDateTimeString(parseInstant(raw.getClosedOn())),
                raw.getLabels(),
                raw.getVotes(),
                toUser(raw.getReporter()),
                toUser(raw.getAssignee()),
                comments
        );
    }

    public static Commit toCommit(CommitRaw raw) {
        if (raw == null) return null;
        return new Commit(
                raw.getHash(),
                raw.getSummary() != null ? raw.getSummary().getRaw().split("\n")[0] : null,
                raw.getMessage(),
                raw.getAuthor() != null ? raw.getAuthor().getUser().getDisplayName() : null,
                raw.getAuthor() != null ? raw.getAuthor().getRawEmail() : null,
                raw.getDate(),
                raw.getLinks() != null && raw.getLinks().getHtml() != null
                        ? raw.getLinks().getHtml().getHref()
                        : null
        );
    }

    public static Project toProject(ProjectRaw raw) {
        if (raw == null || raw.getProject() == null) return null;

        ProjectRaw.InnerProject inner = raw.getProject();

        return new Project(
                inner.getUuid(),
                inner.getName(),
                inner.getLinks() != null && inner.getLinks().getHtml() != null
                        ? inner.getLinks().getHtml().getHref()
                        : null
        );
    }

    private static Instant parseInstant(String dateTime) {
        try {
            return dateTime != null ? Instant.parse(dateTime) : null;
        } catch (Exception e) {
            System.err.println("[!] Failed to parse Instant: " + dateTime);
            return null;
        }
    }

    private static String formatInstantToLocalDateTimeString(Instant instant) {
        return instant != null ? instant.toString().substring(0, 19) : null; // trimite "yyyy-MM-ddTHH:mm:ss"
    }
}
