package com.example.gitminer.service;

import com.example.gitminer.model.*;
import com.example.gitminer.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;

@Service
public class GitMinerService {

    @Value("${gitminer.url}")
    private String gitMinerUrl;

    @Value("${gitminer.token}")
    private String GToken;

    private final ProjectRepository projectRepository;
    private final CommitRepository commitRepository;
    private final IssueRepository issueRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public GitMinerService(ProjectRepository projectRepository,
                           CommitRepository commitRepository,
                           IssueRepository issueRepository,
                           CommentRepository commentRepository,
                           UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.commitRepository = commitRepository;
        this.issueRepository = issueRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    public Project saveProject(Project project) {
        return projectRepository.save(project);
    }

    public Commit saveCommit(Commit commit) {
        // No user objects involved in Commit — no need to attach anything
        return commitRepository.save(commit);
    }

    public Issue saveIssue(Issue issue) {
        // Cache to hold deduplicated users
        Map<String, User> userCache = new HashMap<>();

        // Replace with attached or cached user
        if (issue.getAuthor() != null) {
            issue.setAuthor(attachUserWithCache(issue.getAuthor(), userCache));
        }
        if (issue.getAssignee() != null) {
            issue.setAssignee(attachUserWithCache(issue.getAssignee(), userCache));
        }
        if (issue.getComments() != null) {
            for (Comment comment : issue.getComments()) {
                if (comment.getAuthor() != null) {
                    comment.setAuthor(attachUserWithCache(comment.getAuthor(), userCache));
                }
            }
        }

        return issueRepository.save(issue);
    }

    public Comment saveComment(Comment comment) {
        if (comment.getAuthor() != null) {
            comment.setAuthor(attachUserIfExists(comment.getAuthor()));
        }
        return commentRepository.save(comment);
    }

    private User attachUserIfExists(User user) {
        Optional<User> existing = userRepository.findById(user.getId());
        return existing.orElse(user);
    }

    private User attachUserWithCache(User user, Map<String, User> cache) {
        return cache.computeIfAbsent(user.getId(), id ->
                userRepository.findById(id).orElse(user)
        );
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project getProjectById(String id) {
        return projectRepository.findById(id).orElse(null);
    }

    public List<Commit> getAllCommits() {
        return commitRepository.findAll();
    }

    public Commit getCommitById(String id) {
        return commitRepository.findById(id).orElse(null);
    }

    public List<Issue> getAllIssues() {
        return issueRepository.findAll();
    }

    public Issue getIssueById(String id) {
        return issueRepository.findById(id).orElse(null);
    }

    public List<Issue> getIssuesByStatus(String state) {
        return issueRepository.findByState(state);
    }

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    public Comment getCommentById(String id) {
        return commentRepository.findById(id).orElse(null);
    }

    public List<Comment> getCommentsByIssueId(String issueId) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Issue not found: " + issueId));
        return issue.getComments();
    }

}
