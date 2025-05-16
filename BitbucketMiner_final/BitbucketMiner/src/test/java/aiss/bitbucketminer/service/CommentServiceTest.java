package aiss.bitbucketminer.service;

import aiss.bitbucketminer.model.miner.Comment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CommentServiceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private CommentService commentService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void contextLoads() {
        System.out.println("✅ Serverul rulează pe portul: " + port);
    }

    @Test
    public void getComments_shouldReturnComments() {
        List<Comment> comments = commentService.getComments("gentlero", "bitbucket-api", "87"); // Issue ID "87"

        assertNotNull(comments, "Comments list should not be null");
        System.out.println(">>> Found " + comments.size() + " comments");
        if (!comments.isEmpty()) {
            System.out.println(">>> First comment body: " + comments.get(0).getBody());
        }
    }

    @Test
    public void testGetCommentsViaHttp() {
        String url = "http://localhost:" + port + "/comment/gentlero/bitbucket-api/87"; // Issue ID "87"

        Comment[] response = restTemplate.getForObject(url, Comment[].class);

        assertNotNull(response, "Response should not be null");
        System.out.println("✅ HTTP GET request successful! Comments found: " + response.length);
    }
}
