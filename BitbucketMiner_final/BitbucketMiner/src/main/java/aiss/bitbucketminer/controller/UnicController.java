package aiss.bitbucketminer.controller;

import aiss.bitbucketminer.model.miner.*;
import aiss.bitbucketminer.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/bitbucket")
public class UnicController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private CommitService commitService;

    @Autowired
    private IssueService issueService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${gitminer.url}")
    private String gitMinerUrl;

    @GetMapping("/{workspace}/{repoSlug}")
    public Project getProject(@PathVariable String workspace, @PathVariable String repoSlug) {
        Project project = projectService.getProject(workspace, repoSlug);
        project.setCommits(commitService.getCommits(workspace, repoSlug, 5, 2));
        project.setIssues(issueService.getIssues(workspace, repoSlug));
        return project;
    }

    @GetMapping("/{workspace}/{repoSlug}/commits")
    public ResponseEntity<List<Commit>> getCommits(
            @PathVariable String workspace,
            @PathVariable String repoSlug,
            @RequestParam(defaultValue = "5") int nCommits,
            @RequestParam(defaultValue = "2") int maxPages) {

        List<Commit> commits = commitService.getCommits(workspace, repoSlug, nCommits, maxPages);
        return commits == null
                ? ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of())
                : ResponseEntity.ok(commits);
    }

    @GetMapping("/{workspace}/{repoSlug}/issues")
    public ResponseEntity<List<Issue>> getIssues(@PathVariable String workspace,
                                                 @PathVariable String repoSlug) {
        List<Issue> issues = issueService.getIssues(workspace, repoSlug);
        return issues == null
                ? ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of())
                : ResponseEntity.ok(issues);
    }

    @GetMapping("/{workspace}/{repoSlug}/issues/{issueId}/comments")
    public ResponseEntity<List<Comment>> getComments(@PathVariable String workspace,
                                                     @PathVariable String repoSlug,
                                                     @PathVariable String issueId) {
        List<Comment> comments = commentService.getComments(workspace, repoSlug, issueId);
        return comments == null
                ? ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of())
                : ResponseEntity.ok(comments);
    }

    @PostMapping({"/{workspace}/{repoSlug}/send", "/{workspace}/{repoSlug}"})
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> sendProjectToGitMiner(
            @PathVariable String workspace,
            @PathVariable String repoSlug,
            @RequestParam(defaultValue = "5") int nCommits,
            @RequestParam(defaultValue = "5") int nIssues,
            @RequestParam(defaultValue = "2") int maxPages) {

        Project project = projectService.getProject(workspace, repoSlug);
        if (project == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Project not found or failed to fetch.");
        }

        List<Commit> commits = commitService.getCommits(workspace, repoSlug, nCommits, maxPages);
        for (Commit commit : commits) {
            commit.setProject(null);
        }

        List<Issue> issues = issueService.getIssues(workspace, repoSlug);
        if (issues == null) issues = List.of();

        for (Issue issue : issues) {
            List<Comment> comments = commentService.getComments(workspace, repoSlug, issue.getId());
            if (comments != null) {
                for (Comment comment : comments) {
                    comment.setIssue(null);
                    comment.setAuthor(null);
                }
                issue.setComments(comments);
            } else {
                issue.setComments(List.of());
            }

            issue.setProject(null);
            issue.setAuthor(null);
            issue.setAssignee(null);
        }

        project.setCommits(commits);
        project.setIssues(issues);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Project> request = new HttpEntity<>(project, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    gitMinerUrl + "/projects", request, String.class);
            return response;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send full project to GitMiner: " + e.getMessage());
        }
    }
}
