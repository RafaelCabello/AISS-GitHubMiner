package aiss.githubminer.service;

import aiss.githubminer.model.CommentSearch;
import aiss.githubminer.model.IssueSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    RestTemplate restTemplate;

    public List<CommentSearch> findComments(String owner, String repoName, String issueNumber) {
        List<CommentSearch> comments = new ArrayList<>();
        String uri = "https://api.github.com/repos/" + owner + "/" + repoName + "/issues/" + issueNumber + "/comments";
        CommentSearch[] commentsSearch = restTemplate.getForObject(uri, CommentSearch[].class);
        comments.addAll(Arrays.stream(commentsSearch).toList());
        return comments;
    }
}
