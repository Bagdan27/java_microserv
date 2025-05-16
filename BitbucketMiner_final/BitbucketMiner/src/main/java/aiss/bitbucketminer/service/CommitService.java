package aiss.bitbucketminer.service;

import aiss.bitbucketminer.model.miner.Commit;
import aiss.bitbucketminer.model.raw.CommitRaw;
import aiss.bitbucketminer.transformer.Transformer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommitService {

    @Value("${bitbucket.username}")
    private String username;

    @Value("${bitbucket.token}")
    private String token;

    @Value("${bitbucket.base-url}")
    private String baseUrl;

    @Autowired
    private RestTemplate restTemplate;

    public List<Commit> getCommits(String workspace, String repoSlug, int nCommits, int maxPages) {
        List<Commit> allCommits = new ArrayList<>();
        int currentPage = 0;
        String nextUrl = baseUrl + "/repositories/" + workspace + "/" + repoSlug + "/commits";

        try {
            String plainCreds = username + ":" + token;
            String base64Creds = Base64.getEncoder().encodeToString(plainCreds.getBytes(StandardCharsets.UTF_8));

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Basic " + base64Creds);
            headers.set("Accept", "application/json");

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            while (nextUrl != null && currentPage < maxPages && allCommits.size() < nCommits) {
                ResponseEntity<CommitRawListWithPaging> response = restTemplate.exchange(
                        nextUrl,
                        HttpMethod.GET,
                        entity,
                        CommitRawListWithPaging.class
                );

                CommitRawListWithPaging rawList = response.getBody();
                if (rawList != null && rawList.getValues() != null) {
                    List<Commit> pageCommits = rawList.getValues().stream()
                            .map(Transformer::toCommit)
                            .collect(Collectors.toList());

                    allCommits.addAll(pageCommits);
                }

                nextUrl = rawList != null ? rawList.getNext() : null;
                currentPage++;
            }

            return allCommits.stream().limit(nCommits).collect(Collectors.toList());

        } catch (Exception e) {
            System.err.println("Error fetching commits: " + e.getMessage());
            return null;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CommitRawList {
        @JsonProperty("values")
        private List<CommitRaw> values;

        public List<CommitRaw> getValues() {
            return values;
        }

        public void setValues(List<CommitRaw> values) {
            this.values = values;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CommitRawListWithPaging extends CommitRawList {
        @JsonProperty("next")
        private String next;

        public String getNext() {
            return next;
        }

        public void setNext(String next) {
            this.next = next;
        }
    }
}
