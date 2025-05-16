package aiss.bitbucketminer.service;

import aiss.bitbucketminer.model.miner.Comment;
import aiss.bitbucketminer.model.miner.Issue;
import aiss.bitbucketminer.model.raw.CommentRaw;
import aiss.bitbucketminer.model.raw.IssueRaw;
import aiss.bitbucketminer.transformer.Transformer;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IssueService {

    @Value("${bitbucket.username}")
    private String username;

    @Value("${bitbucket.token}")
    private String token;

    @Value("${bitbucket.base-url}")
    private String baseUrl;

    @Autowired
    private RestTemplate restTemplate;

    public List<Issue> getIssues(String workspace, String repoSlug) {
        try {
            String url = baseUrl + "/repositories/" + workspace + "/" + repoSlug + "/issues";

            String plainCreds = username + ":" + token;
            String base64Creds = Base64.getEncoder().encodeToString(plainCreds.getBytes(StandardCharsets.UTF_8));

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Basic " + base64Creds);
            headers.set("Accept", "application/json");

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<IssueRawList> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    IssueRawList.class
            );

            List<IssueRaw> rawIssues = response.getBody() != null ? response.getBody().getValues() : null;
            if (rawIssues == null) return List.of();

            return rawIssues.stream()
                    .map(raw -> Transformer.toIssue(raw, null))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            System.err.println("Error fetching issues: " + e.getMessage());
            return List.of();
        }
    }

    private static class IssueRawList {
        @JsonProperty("values")
        private List<IssueRaw> values;

        public List<IssueRaw> getValues() {
            return values;
        }

        public void setValues(List<IssueRaw> values) {
            this.values = values;
        }
    }
}
