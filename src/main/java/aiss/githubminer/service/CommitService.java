package aiss.githubminer.service;

import aiss.githubminer.model.Commit;
import aiss.githubminer.model.CommitSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CommitService {

    @Autowired
    RestTemplate restTemplate;

    public List<CommitSearch> findAllCommits(String owner, String repoName) {
        List<CommitSearch> commits = new ArrayList<>();
        String uri = "https://api.github.com/repos/" + owner + "/" + repoName + "/commits";
        CommitSearch[] commitsSearch = restTemplate.getForObject(uri, CommitSearch[].class);
        commits.addAll(Arrays.stream(commitsSearch).toList());
        return commits;
    }
}
