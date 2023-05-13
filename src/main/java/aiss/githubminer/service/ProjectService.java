package aiss.githubminer.service;

import aiss.githubminer.model.*;
import aiss.githubminer.modelPost.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectService {

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    CommitService commitService;

    @Autowired
    IssueService issueService;

    @Autowired
    CommentService commentService;
    @Autowired
    UserService userService;

    public ProjectSearch findOne(String owner, String repoName) {
        String uri = "https://api.github.com/repos/" + owner + "/" + repoName;
        ProjectSearch projectSearch = restTemplate.getForObject(uri, ProjectSearch.class);
        return projectSearch;
    }

    public Project getProject(String owner, String repoName, Integer maxPages) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + "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");
        HttpEntity<ProjectSearch> request = new HttpEntity<ProjectSearch>(headers);

        String uri = "https://api.github.com/repos/" + owner + "/" + repoName;
        ResponseEntity<ProjectSearch> response = restTemplate.exchange(uri, HttpMethod.GET, request, ProjectSearch.class);
        ProjectSearch projectSearch = response.getBody();
        Project project = new Project(projectSearch.getId().toString(), projectSearch.getName(), projectSearch.getHtmlUrl());

        //Add commits
        List<CommitSearch> commitsSearch = commitService.findProjectCommits(owner, repoName, maxPages);
        List<Commit> commits = new ArrayList<>();
        for (CommitSearch commitSearch: commitsSearch) {
            String titulo = commitSearch.getCommit().getMessage().split("\n")[0];
            Commit newCommit = new Commit(
                    commitSearch.getSha(),
                    titulo,
                    commitSearch.getCommit().getMessage(),
                    commitSearch.getCommit().getAuthor().getName(),
                    commitSearch.getCommit().getAuthor().getEmail(),
                    commitSearch.getCommit().getAuthor().getDate(),
                    commitSearch.getCommit().getCommitter().getName(),
                    commitSearch.getCommit().getCommitter().getEmail(),
                    commitSearch.getCommit().getCommitter().getDate(),
                    commitSearch.getHtmlUrl()
            );
            commits.add(newCommit);
        }
        project.setCommits(commits);

        //Add issues

        List<IssueSearch> issuesSearch = issueService.findByProject(owner, repoName, maxPages);
        List<Issue> issues = new ArrayList<>();
        for (IssueSearch issueSearch: issuesSearch) {
            // control closedAt null
            String closed = null;
            if (issueSearch.getClosedAt() != null){
                closed = issueSearch.getClosedAt().toString();
            }

            //Extraer el nombre de la label
            List<String> labels = new ArrayList<>();
            for (Label l: issueSearch.getLabels()){
                labels.add(l.getName());
            }

            //buscar author
            UserUnit foundUser = userService.findUser(issueSearch.getUser().getLogin());
            User author = new User(foundUser.getId(),
                    foundUser.getLogin(),
                    foundUser.getName(),
                    foundUser.getAvatarUrl(),
                    foundUser.getHtmlUrl());

            //buscar asignee
            User asignee = null;
            if (issueSearch.getAssignee() != null) {
                UserUnit foundAsignee = userService.findUser(issueSearch.getUser().getLogin());
                asignee = new User(foundUser.getId(),
                        foundUser.getLogin(),
                        foundUser.getName(),
                        foundUser.getAvatarUrl(),
                        foundUser.getHtmlUrl());
            }

            Issue newIssue = new Issue(issueSearch.getId(),
                    issueSearch.getNumber(),
                    issueSearch.getTitle(),
                    issueSearch.getBody(),
                    issueSearch.getState(),
                    issueSearch.getCreatedAt(),
                    issueSearch.getUpdatedAt(),
                    closed,
                    labels,
                    author,
                    asignee,
                    0,
                    0,
                    issueSearch.getHtml_url());
            //Add comments
            List<CommentSearch> commentsSearch = commentService.findByProjectIssue(owner, repoName, newIssue.getRefId().toString(), maxPages);
            List<Comment> comments = new ArrayList<>();
            for (CommentSearch commentSearch: commentsSearch) {
                //buscar commentAuthor
                UserUnit commentAuthor = userService.findUser(commentSearch.getUser().getLogin());
                User newAuthor = new User(foundUser.getId(),
                        foundUser.getLogin(),
                        foundUser.getName(),
                        foundUser.getAvatarUrl(),
                        foundUser.getHtmlUrl());

                Comment newComment = new Comment(
                        commentSearch.getId().toString(),
                        commentSearch.getBody(),
                        commentSearch.getCreatedAt(),
                        commentSearch.getUpdatedAt(),
                        newAuthor
                );
                comments.add(newComment);
            }
            newIssue.setComments(comments);
            issues.add(newIssue);
        }
        project.setIssues(issues);

        return project;
    }
}
