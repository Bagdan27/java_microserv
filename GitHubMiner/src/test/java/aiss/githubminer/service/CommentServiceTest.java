package aiss.githubminer.service;

import aiss.githubminer.model.flat.CommentFlat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Test
    void testSendToGitMinerSimulated() {
        CommentFlat comment = new CommentFlat(
                "cmt001",
                "This is a test comment for GitMiner.",
                "2024-04-01T10:00:00Z",
                "2024-04-01T10:30:00Z",
                "octocat",
                "https://github.com/octocat/Hello-World/issues/1#issuecomment-1"
        );

        CommentFlat result = commentService.sendToGitMiner(comment);

        assertNotNull(result, "Returned comment should not be null");
        assertEquals(comment.getId(), result.getId(), "Comment ID should match");
        assertEquals(comment.getAuthor(), result.getAuthor(), "Comment author should match");
        System.out.println("✅ Simulated comment sent: " + result.getBody());
    }
}
