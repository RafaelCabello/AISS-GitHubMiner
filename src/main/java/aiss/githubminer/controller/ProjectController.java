package aiss.githubminer.controller;

import aiss.githubminer.modelPost.Commit;
import aiss.githubminer.modelPost.Issue;
import aiss.githubminer.modelPost.Project;
import aiss.githubminer.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/githubminer")
public class ProjectController {
    @Autowired
    RestTemplate template;
    @Autowired
    ProjectService projectService;

    //POST http://localhost:8082/githubminer/{owner}/{repoName}[?sinceCommits=5&sinceIssues=30&maxPages=2]
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{owner}/{repoName}")
    public Project create(@PathVariable String owner, @PathVariable String repoName, @RequestParam(defaultValue = "2") Long sinceCommits
            , @RequestParam(defaultValue = "20") Long sinceIssues, @RequestParam(defaultValue = "2") Integer maxPages,
                          @RequestHeader("Authorization") String token) {
        Project _project = projectService.getProject(owner, repoName, maxPages, token);
        List<Commit> commits = _project.getCommits().stream()
                .filter(c -> LocalDateTime.parse(c.getCommitted_date().substring(0,19)).isAfter(LocalDateTime.now().minusDays(sinceCommits))).toList();
        List<Issue> issues = _project.getIssues().stream()
                .filter(i -> LocalDateTime.parse(i.getUpdatedAt().substring(0,19)).isAfter(LocalDateTime.now().minusDays(sinceIssues))).toList();
        Project filtredProject = new Project(_project.getId(), _project.getName(), _project.getWebUrl(), commits, issues);
        String uri = "http://localhost:8080/gitminer/projects";
        Project createdProject = template.postForObject(uri, filtredProject, Project.class);
        return createdProject;
    }

    //GET http://localhost:8082/githubminer/{owner}/{repoName}[?sinceCommits=5&sinceIssues=30&maxPages=2]
    @GetMapping("/{owner}/{repoName}")
    public Project findProject(@PathVariable String owner, @PathVariable String repoName, @RequestParam(defaultValue = "2") Long sinceCommits
            , @RequestParam(defaultValue = "20") Long sinceIssues, @RequestParam(defaultValue = "2") Integer maxPages,
                               @RequestHeader("Authorization") String token) {
        Project _project = projectService.getProject(owner, repoName, maxPages, token);
        List<Commit> commits = _project.getCommits().stream()
                .filter(c -> LocalDateTime.parse(c.getCommitted_date().substring(0,19)).isAfter(LocalDateTime.now().minusDays(sinceCommits))).toList();
        List<Issue> issues = _project.getIssues().stream()
                .filter(i -> LocalDateTime.parse(i.getUpdatedAt().substring(0,19)).isAfter(LocalDateTime.now().minusDays(sinceIssues))).toList();
        Project filtredProject = new Project(_project.getId(), _project.getName(), _project.getWebUrl(), commits, issues);
        return filtredProject;
    }

}
