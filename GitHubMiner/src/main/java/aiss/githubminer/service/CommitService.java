package aiss.githubminer.service;

import aiss.githubminer.model.Commit;
import aiss.githubminer.model.flat.CommitFlat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommitService {

    @Value("${github.token}")
    private String githubToken;

    @Value("${gitminer.url}")
    private String gitMinerUrl;

    private static final String GITHUB_API_URL = "https://api.github.com/repos";

    @Autowired
    private RestTemplate restTemplate;

    public List<CommitFlat> findCommitsFromGitHub(String owner, String repo, int sinceDays, int maxPages) {
        String sinceDate = LocalDateTime.now()
                .minusDays(sinceDays)
                .atZone(ZoneOffset.UTC)
                .format(DateTimeFormatter.ISO_INSTANT);

        List<CommitFlat> results = new ArrayList<>();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(githubToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        for (int page = 1; page <= maxPages; page++) {
            String url = String.format(
                    "%s/%s/%s/commits?since=%s&page=%d",
                    GITHUB_API_URL, owner, repo, sinceDate, page
            );

            ResponseEntity<Commit[]> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, Commit[].class
            );

            Commit[] commits = response.getBody();
            if (commits == null || commits.length == 0) break;

            results.addAll(Arrays.stream(commits)
                    .map(Commit::toFlat)
                    .toList());
            if (commits.length < 30) break;
        }

        return results;
    }

    public CommitFlat sendToGitMiner(CommitFlat commit) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(githubToken);

        HttpEntity<CommitFlat> request = new HttpEntity<>(commit, headers);
        try {
            ResponseEntity<CommitFlat> response = restTemplate.postForEntity(
                    gitMinerUrl + "/commits",
                    request,
                    CommitFlat.class
            );
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Failed to send commit to GitMiner: " + e.getMessage());
        }
    }

    public List<CommitFlat> sendToGitMinerBatch(List<CommitFlat> commits) {
        return commits.stream()
                .map(this::sendToGitMiner)
                .collect(Collectors.toList());
    }
}
