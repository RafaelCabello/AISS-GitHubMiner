package aiss.githubminer.service;

import aiss.githubminer.model.ProjectSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProjectService {

    @Autowired
    RestTemplate restTemplate;

    public ProjectSearch findOne(String owner, String repoName) {
        String uri = "https://api.github.com/repos/" + owner + "/" + repoName;
        System.out.println(uri);
        ProjectSearch projectSearch = restTemplate.getForObject(uri, ProjectSearch.class);
        return projectSearch;
    }

}
