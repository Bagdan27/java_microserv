package aiss.githubminer.service;

import aiss.githubminer.model.Project;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProjectServiceTest {

    @Autowired
    private ProjectService projectService;

    @Test
    void testFindAllProjects() {
        List<Project> projects = projectService.findAllProjects();
        assertNotNull(projects, "Returned project list should not be null");
        assertFalse(projects.isEmpty(), "Mock project list should not be empty");
    }

    @Test
    void testFindProjectFromGitHub() {
        Project project = projectService.findProjectFromGitHub("octocat", "Hello-World");
        assertNotNull(project, "GitHub project should not be null");
        assertEquals("octocat/Hello-World", project.getName(), "Project name mismatch");
    }

    @Test
    void testSendToGitMinerSimulated() {
        Project p = new Project();
        p.setId(12345);
        p.setName("test/project");
        p.setWebUrl("https://github.com/test/project");

        Project result = projectService.sendToGitMiner(p);
        assertEquals(p.getName(), result.getName(), "Simulated send should return the same project");
    }
}
