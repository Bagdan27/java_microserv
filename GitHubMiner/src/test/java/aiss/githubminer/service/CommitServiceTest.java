package aiss.githubminer.service;

import aiss.githubminer.model.flat.CommitFlat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommitServiceTest {

    @Autowired
    private CommitService commitService;

    @Test
    void testFindCommitsFromRealRepo() {
        List<CommitFlat> commits = commitService.findCommitsFromGitHub("torvalds", "linux", 30, 1);
        assertNotNull(commits, "Returned list should not be null");
        assertFalse(commits.isEmpty(), "Expected commits from torvalds/linux");
        assertNotNull(commits.get(0).getId(), "Commit ID should not be null");
        assertNotNull(commits.get(0).getTitle(), "Commit title should not be null");
        System.out.println("✅ Found " + commits.size() + " commits");
    }
}
