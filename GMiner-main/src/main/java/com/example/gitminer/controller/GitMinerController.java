package com.example.gitminer.controller;

import com.example.gitminer.model.Comment;
import com.example.gitminer.model.Issue;
import com.example.gitminer.model.Project;
import com.example.gitminer.model.User;
import com.example.gitminer.service.GitMinerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/gitminer")
public class GitMinerController {

    @Value("${gitminer.url}")
    private String gitMinerUrl;

    @Value("${gitminer.token}")
    private String GToken;

    @Autowired
    private GitMinerService gitMinerService;

    @PostMapping("/projects")
    @ResponseStatus(HttpStatus.CREATED)
    public Project addProject(@RequestBody Project project) {
        dedupeUsers(project);
        return gitMinerService.saveProject(project);
    }


    private void dedupeUsers(Project project) {
        Map<String, User> userCache = new HashMap<>();
        if (project.getIssues() != null) {
            for (Issue issue : project.getIssues()) {
                // Dedupe issue author
                User author = issue.getAuthor();
                if (author != null && author.getId() != null) {
                    issue.setAuthor(userCache.computeIfAbsent(author.getId(), key -> author));
                }
                // Dedupe issue assignee
                User assignee = issue.getAssignee();
                if (assignee != null && assignee.getId() != null) {
                    issue.setAssignee(userCache.computeIfAbsent(assignee.getId(), key -> assignee));
                }
                // Dedupe comment authors
                if (issue.getComments() != null) {
                    for (Comment comment : issue.getComments()) {
                        User commentAuthor = comment.getAuthor();
                        if (commentAuthor != null && commentAuthor.getId() != null) {
                            comment.setAuthor(userCache.computeIfAbsent(commentAuthor.getId(), key -> commentAuthor));
                        }
                    }
                }
            }
        }
    }

    @PostMapping("/commits")
    public com.example.gitminer.model.Commit addCommit(@RequestBody com.example.gitminer.model.Commit commit) {
        return gitMinerService.saveCommit(commit);
    }

    @PostMapping("/issues")
    public Issue addIssue(@RequestBody Issue issue) {
        return gitMinerService.saveIssue(issue);
    }

    @PostMapping("/comments")
    public Comment addComment(@RequestBody Comment comment) {
        return gitMinerService.saveComment(comment);
    }

    @GetMapping("/projects")
    public List<Project> getAllProjects() {
        return gitMinerService.getAllProjects();
    }

    @GetMapping("/projects/{id}")
    public Project getProjectById(@PathVariable String id) {
        return gitMinerService.getProjectById(id);
    }

    @GetMapping("/commits")
    public List<com.example.gitminer.model.Commit> getAllCommits() {
        return gitMinerService.getAllCommits();
    }

    @GetMapping("/commits/{id}")
    public com.example.gitminer.model.Commit getCommitById(@PathVariable String id) {
        return gitMinerService.getCommitById(id);
    }

    @GetMapping("/issues")
    public List<Issue> getIssues(@RequestParam(required = false) String status) {
        return (status == null)
                ? gitMinerService.getAllIssues()
                : gitMinerService.getIssuesByStatus(status);
    }

    @GetMapping("/issues/{id}")
    public Issue getIssueById(@PathVariable String id) {
        return gitMinerService.getIssueById(id);
    }

    @GetMapping("/comments")
    public List<Comment> getAllComments() {
        return gitMinerService.getAllComments();
    }

    @GetMapping("/comments/{id}")
    public Comment getCommentById(@PathVariable String id) {
        return gitMinerService.getCommentById(id);
    }

    @GetMapping("/issues/{issueId}/comments")
    public List<Comment> getCommentsByIssue(@PathVariable String issueId) {
        return gitMinerService.getCommentsByIssueId(issueId);
    }

}
