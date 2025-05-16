package aiss.bitbucketminer.service;

import aiss.bitbucketminer.model.miner.Project;
import aiss.bitbucketminer.model.raw.ProjectRaw;
import aiss.bitbucketminer.transformer.Transformer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ProjectServiceTest {

    private ProjectService projectService;
    private RestTemplate restTemplate;

    @BeforeEach
    public void setup() {
        restTemplate = mock(RestTemplate.class);
        String baseUrl = "https://api.bitbucket.org/2.0";
        String username = "testuser";
        String token = "testtoken";

        projectService = new ProjectService(baseUrl, username, token, restTemplate);
    }

    @Test
    public void testGetProject_successful() {
        // Arrange: mock ProjectRaw
        ProjectRaw.InnerProject innerProject = new ProjectRaw.InnerProject();
        innerProject.setUuid("uuid-1234");
        innerProject.setName("Test Project");

        ProjectRaw.Links links = new ProjectRaw.Links();
        ProjectRaw.Html html = new ProjectRaw.Html();
        html.setHref("https://bitbucket.org/testWorkspace/testRepo");
        links.setHtml(html);
        innerProject.setLinks(links);

        ProjectRaw raw = new ProjectRaw();
        raw.setProject(innerProject);

        // Simulează răspunsul HTTP
        ResponseEntity<ProjectRaw> mockResponse = new ResponseEntity<>(raw, HttpStatus.OK);
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(ProjectRaw.class))
        ).thenReturn(mockResponse);

        // Act
        Project project = projectService.getProject("testWorkspace", "testRepo");

        // Assert
        assertNotNull(project);
        assertEquals("uuid-1234", project.getId());
        assertEquals("Test Project", project.getName());
        assertEquals("https://bitbucket.org/testWorkspace/testRepo", project.getWebUrl());
    }

    @Test
    public void testGetProject_returnsNullIfError() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(ProjectRaw.class)))
                .thenThrow(new RuntimeException("Bitbucket API error"));

        Project result = projectService.getProject("ws", "repo");

        assertNull(result);
    }
}
