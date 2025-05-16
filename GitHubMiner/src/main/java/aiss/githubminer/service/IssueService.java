package aiss.githubminer.service;

import aiss.githubminer.model.Issue;
import aiss.githubminer.model.flat.IssueFlat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IssueService {

    @Value("${github.token}")
    private String githubToken;

    @Value("${gitminer.url}")
    private String gitMinerUrl;

    private static final String GITHUB_API_URL = "https://api.github.com/repos";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public List<IssueFlat> findIssuesFromGitHub(String owner, String repo, int sinceDays) {
        String sinceDate = LocalDateTime.now()
                .minusDays(sinceDays)
                .atZone(ZoneOffset.UTC)
                .format(DateTimeFormatter.ISO_INSTANT);

        String url = String.format(
                "%s/%s/%s/issues?state=all&since=%s",
                GITHUB_API_URL, owner, repo, sinceDate
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(githubToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Issue[]> response = restTemplate.exchange(
                url,
                org.springframework.http.HttpMethod.GET,
                entity,
                Issue[].class
        );

        Issue[] issues = response.getBody();
        if (issues == null) {
            return List.of();
        }

        return Arrays.stream(issues)
                .map(Issue::toFlat)
                .collect(Collectors.toList());
    }

    private String stripZone(String ts) {
        if (ts == null) return null;
        return ts.endsWith("Z") ? ts.substring(0, ts.length() - 1) : ts;
    }

    private IssueFlat sanitizeDates(IssueFlat issue) {
        issue.setCreatedAt(stripZone(issue.getCreatedAt()));
        issue.setUpdatedAt(stripZone(issue.getUpdatedAt()));
        issue.setClosedAt(stripZone(issue.getClosedAt()));
        return issue;
    }

    public IssueFlat sendToGitMiner(IssueFlat issue) {
        IssueFlat safe = sanitizeDates(issue);
        safe.setComments(Collections.emptyList());

        safe.setComments(Collections.emptyList());
        safe.setAssignee(null);


        String jsonPayload;
        try {
            jsonPayload = objectMapper.writeValueAsString(safe);
            System.out.println(">>> Debug payload to GitMiner: " + jsonPayload);
        } catch (JsonProcessingException e) {
            jsonPayload = "<failed to serialize payload>";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(githubToken);
        HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers);

        try {
            ResponseEntity<IssueFlat> response = restTemplate.postForEntity(
                    gitMinerUrl + "/issues",
                    request,
                    IssueFlat.class
            );
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to send issue to GitMiner: " + e.getMessage() + "; payload was: " + jsonPayload
            );
        }
    }


    public List<IssueFlat> sendToGitMinerBatch(List<IssueFlat> issues) {
        return issues.stream()
                .map(this::sendToGitMiner)
                .collect(Collectors.toList());
    }
}
