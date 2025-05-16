package aiss.bitbucketminer.service;

import aiss.bitbucketminer.model.miner.Comment;
import aiss.bitbucketminer.model.raw.CommentRaw;
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
public class CommentService {

    @Value("${bitbucket.username}")
    private String username;

    @Value("${bitbucket.token}")
    private String token;

    @Value("${bitbucket.base-url}")
    private String baseUrl;

    @Autowired
    private RestTemplate restTemplate;

    public List<Comment> getComments(String workspace, String repoSlug, String issueId) {
        try {
            String url = baseUrl + "/repositories/" + workspace + "/" + repoSlug + "/issues/" + issueId + "/comments";

            String plainCreds = username + ":" + token;
            String base64Creds = Base64.getEncoder().encodeToString(plainCreds.getBytes(StandardCharsets.UTF_8));

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Basic " + base64Creds);
            headers.set("Accept", "application/json");

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<CommentRawList> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    CommentRawList.class
            );

            if (response.getBody() != null && response.getBody().getValues() != null) {
                return response.getBody().getValues()
                        .stream()
                        .map(Transformer::toComment)
                        .collect(Collectors.toList());
            } else {
                return List.of();
            }

        } catch (Exception e) {
            System.err.println("Error fetching comments: " + e.getMessage());
            return List.of();
        }
    }

    public static class CommentRawList {
        @JsonProperty("values")
        private List<CommentRaw> values;

        public List<CommentRaw> getValues() {
            return values;
        }

        public void setValues(List<CommentRaw> values) {
            this.values = values;
        }
    }
}
