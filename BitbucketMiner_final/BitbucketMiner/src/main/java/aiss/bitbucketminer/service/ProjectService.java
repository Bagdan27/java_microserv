package aiss.bitbucketminer.service;

import aiss.bitbucketminer.model.miner.Project;
import aiss.bitbucketminer.model.raw.ProjectRaw;
import aiss.bitbucketminer.transformer.Transformer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class ProjectService {

    private final String username;
    private final String token;
    private final String baseUrl;
    private final RestTemplate restTemplate;

    public ProjectService(
            @Value("${bitbucket.username}") String username,
            @Value("${bitbucket.token}") String token,
            @Value("${bitbucket.base-url}") String baseUrl,
            RestTemplate restTemplate) {
        this.username = username;
        this.token = token;
        this.baseUrl = baseUrl;
        this.restTemplate = restTemplate;
    }

    public Project getProject(String workspace, String repoSlug) {
        try {
            String url = baseUrl + "/repositories/" + workspace + "/" + repoSlug;

            String plainCreds = username + ":" + token;
            String base64Creds = Base64.getEncoder().encodeToString(plainCreds.getBytes(StandardCharsets.UTF_8));

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Basic " + base64Creds);
            headers.set("Accept", "application/json");

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<ProjectRaw> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    ProjectRaw.class
            );

            ProjectRaw raw = response.getBody();
            if (raw == null) {
                System.err.println("Warning: Received null ProjectRaw");
                return null;
            }

            return Transformer.toProject(raw);

        } catch (Exception e) {
            System.err.println("Error fetching project: " + e.getMessage());
            return null;
        }
    }
}
