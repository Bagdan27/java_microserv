package aiss.bitbucketminer.service;

import aiss.bitbucketminer.model.miner.Issue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IssueServiceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private IssueService issueService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void contextLoads() {
        System.out.println("✅ Serverul rulează pe portul: " + port);
    }

    @Test
    public void getIssues_shouldReturnIssues() {
        List<Issue> issues = issueService.getIssues("gentlero", "bitbucket-api");
        assertNotNull(issues, "Issues list should not be null");
        assertFalse(issues.isEmpty(), "Issues list should not be empty");

        Issue first = issues.get(0);
        assertNotNull(first.getId(), "Issue ID should not be null");
        assertNotNull(first.getTitle(), "Issue title should not be null");

        System.out.println("✅ Issues retrieved successfully!");
    }

    @Test
    public void testGetIssuesViaHttp() {
        String url = "http://localhost:" + port + "/issues/gentlero/bitbucket-api";
        Issue[] response = restTemplate.getForObject(url, Issue[].class);

        assertNotNull(response, "Response should not be null");
        assertTrue(response.length > 0, "Response should not be empty");

        System.out.println("✅ HTTP GET /issues successful!");
    }
}
