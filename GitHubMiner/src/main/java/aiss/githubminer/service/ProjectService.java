package aiss.githubminer.service;

import aiss.githubminer.model.Project;
import aiss.githubminer.model.flat.CommitFlat;
import aiss.githubminer.model.flat.IssueFlat;
import aiss.githubminer.model.flat.ProjectFlat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ProjectService {

    @Value("${github.token}")
    private String githubToken;

    @Value("${gitminer.url}")
    private String gitMinerUrl;

    private final RestTemplate restTemplate;

    @Autowired
    public ProjectService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Project findProjectFromGitHub(String owner, String repo) {
        String url = "https://api.github.com/repos/" + owner + "/" + repo;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(githubToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Project> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Project.class
        );

        return response.getBody();
    }

    public Project sendToGitMiner(Project project) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(githubToken);

        HttpEntity<Project> request = new HttpEntity<>(project, headers);

        ResponseEntity<Project> response = restTemplate.postForEntity(
                gitMinerUrl + "/projects",
                request,
                Project.class
        );

        return response.getBody();
    }

    public List<Project> findAllProjects() {
        Project p = new Project();
        p.setId("1296269");
        p.setName("octocat/Hello-World");
        p.setWebUrl("https://github.com/octocat/Hello-World");

        return List.of(p);
    }

    public ProjectFlat buildProjectFlat(String owner,
                                        String repo,
                                        List<CommitFlat> commits,
                                        List<IssueFlat> issues) {
        Project project = findProjectFromGitHub(owner, repo);
        ProjectFlat pf = new ProjectFlat();
        pf.setId(project.getId());
        pf.setName(project.getName());
        pf.setWebUrl(project.getWebUrl());
        pf.setCommits(commits);
        pf.setIssues(issues);
        return pf;
    }
}
