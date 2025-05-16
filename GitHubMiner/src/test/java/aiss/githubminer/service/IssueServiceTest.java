package aiss.githubminer.service;

import aiss.githubminer.model.flat.IssueFlat;
import aiss.githubminer.service.IssueService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureMockMvc
class IssueServiceTest {

    @Autowired
    IssueService issueService;

    @Test
    void testFindIssuesFromGitHub() {
        List<IssueFlat> issues = issueService.findIssuesFromGitHub("octocat", "Hello-World", 10);
        assertNotNull(issues);
        assertFalse(issues.isEmpty());
    }
}
