package aiss.bitbucketminer.service;

import aiss.bitbucketminer.model.miner.Commit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CommitServiceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private CommitService commitService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void contextLoads() {
        System.out.println("✅ Serverul rulează pe portul: " + port);
    }

    @Test
    public void getCommits_shouldReturnCommits() {
        // Test direct pe service
        List<Commit> commits = commitService.getCommits("gentlero", "bitbucket-api", 5, 2);

        assertNotNull(commits, "Commits list should not be null");
        assertFalse(commits.isEmpty(), "Commits list should not be empty");

        Commit firstCommit = commits.get(0);
        assertNotNull(firstCommit.getId(), "Commit ID should not be null");
        assertNotNull(firstCommit.getMessage(), "Commit message should not be null");

        System.out.println(">>> First commit ID: " + firstCommit.getId());
        System.out.println(">>> First commit message: " + firstCommit.getMessage());
    }

    @Test
    public void testGetCommitsViaHttp() {
        // Construim URL-ul endpointului
        String url = "http://localhost:" + port + "/commits/gentlero/bitbucket-api";

        // Facem request GET la endpoint
        Commit[] response = restTemplate.getForObject(url, Commit[].class);

        // Verificăm că răspunsul nu este null și conține date
        assertNotNull(response, "Response should not be null");
        assertTrue(response.length > 0, "Response array should not be empty");

        Commit firstCommit = response[0];
        assertNotNull(firstCommit.getId(), "Commit ID should not be null");
        assertNotNull(firstCommit.getMessage(), "Commit message should not be null");

        System.out.println("✅ HTTP GET commits request successful!");
        System.out.println(">>> First commit ID (via HTTP): " + firstCommit.getId());
    }
}
