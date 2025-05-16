package aiss.githubminer.controller;

import aiss.githubminer.model.Project;
import aiss.githubminer.model.flat.CommentFlat;
import aiss.githubminer.model.flat.CommitFlat;
import aiss.githubminer.model.flat.IssueFlat;
import aiss.githubminer.model.flat.ProjectFlat;
import aiss.githubminer.service.CommentService;
import aiss.githubminer.service.CommitService;
import aiss.githubminer.service.IssueService;
import aiss.githubminer.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/github")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private CommitService commitService;

    @Autowired
    private IssueService issueService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/{owner}/{repo}")
    public ProjectFlat getProjectFlat(
            @PathVariable String owner,
            @PathVariable String repo,
            @RequestParam(defaultValue = "2") int sinceCommits,
            @RequestParam(defaultValue = "20") int sinceIssues,
            @RequestParam(defaultValue = "2") int maxPages
    ) {

        List<CommitFlat> commits = commitService.findCommitsFromGitHub(owner, repo, sinceCommits, maxPages);
        List<IssueFlat> issues = issueService.findIssuesFromGitHub(owner, repo, sinceIssues);


        issues.forEach(issue -> {
            List<CommentFlat> comments = commentService.findCommentsFromGitHub(
                    owner,
                    repo,
                    String.valueOf(issue.getNumber())
            );
            issue.setComments(comments);
        });


        return projectService.buildProjectFlat(owner, repo, commits, issues);
    }


    @PostMapping("/{owner}/{repo}")
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectFlat mineAndSendProject(
            @PathVariable String owner,
            @PathVariable String repo,
            @RequestParam(defaultValue = "2") int sinceCommits,
            @RequestParam(defaultValue = "20") int sinceIssues,
            @RequestParam(defaultValue = "2") int maxPages
    ) {

        ProjectFlat projectFlat = getProjectFlat(owner, repo, sinceCommits, sinceIssues, maxPages);


        Project projectMeta = projectService.findProjectFromGitHub(owner, repo);
        projectService.sendToGitMiner(projectMeta);


        commitService.sendToGitMinerBatch(projectFlat.getCommits());


        issueService.sendToGitMinerBatch(projectFlat.getIssues());


        List<CommentFlat> allComments = projectFlat.getIssues().stream()
                .flatMap(i -> i.getComments().stream())
                .collect(Collectors.toList());
        commentService.sendToGitMinerBatch(allComments);

        return projectFlat;
    }
}
