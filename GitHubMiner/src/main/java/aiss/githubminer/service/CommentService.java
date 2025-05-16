package aiss.githubminer.service;

import aiss.githubminer.model.Comment;
import aiss.githubminer.model.flat.CommentFlat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Value("${github.token}")
    private String githubToken;

    @Value("${gitminer.url}")
    private String gitMinerUrl;

    @Autowired
    private RestTemplate restTemplate;

    public List<CommentFlat> findCommentsFromGitHub(String owner, String repo, String issueNumber) {
        String url = String.format(
                "https://api.github.com/repos/%s/%s/issues/%s/comments",
                owner, repo, issueNumber
        );
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(githubToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Comment[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Comment[].class
        );
        Comment[] comments = response.getBody();
        if (comments == null) {
            return List.of();
        }
        return Arrays.stream(comments)
                .map(Comment::toFlat)
                .collect(Collectors.toList());
    }

    public CommentFlat sendToGitMiner(CommentFlat comment) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(githubToken);

        HttpEntity<CommentFlat> requestEntity = new HttpEntity<>(comment, headers);
        try {
            ResponseEntity<CommentFlat> response = restTemplate.postForEntity(
                    gitMinerUrl + "/comments",
                    requestEntity,
                    CommentFlat.class
            );
            return response.getBody();
        } catch (Exception e) {
            System.err.println("Skipping comment " + comment.getId() + ": " + e.getMessage());
            return null;
        }
    }

    public List<CommentFlat> sendToGitMinerBatch(List<CommentFlat> comments) {
        return comments.stream()
                .map(this::sendToGitMiner)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
