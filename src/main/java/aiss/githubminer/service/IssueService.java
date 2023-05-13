package aiss.githubminer.service;

import aiss.githubminer.model.IssueSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class IssueService {
    @Autowired
    RestTemplate restTemplate;

    public List<IssueSearch> findAllIssues(String owner, String repoName) {
        List<IssueSearch> issues = new ArrayList<>();
        String uri = "https://api.github.com/repos/" + owner + "/" + repoName + "/issues";
        IssueSearch[] issuesSearch = restTemplate.getForObject(uri, IssueSearch[].class);
        issues.addAll(Arrays.stream(issuesSearch).toList());
        return issues;
    }
}
