package aiss.githubminer.service;

import aiss.githubminer.model.IssueSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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

    public List<IssueSearch> findByProject(String owner, String repoName, Integer maxPages){
        String uri = "https://api.github.com/repos/" + owner + "/" + repoName + "/issues";

        String token = "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";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer" + token);
        HttpEntity<IssueSearch[]> request = new HttpEntity<>(null, headers);

        ResponseEntity<IssueSearch[]> response = restTemplate.exchange(uri, HttpMethod.GET, request, IssueSearch[].class);

        List<IssueSearch> issues = new ArrayList<>(Arrays.asList(response.getBody()));

        String nextUrl = getNextPageUrl(response.getHeaders());
        int i = 1;
        while (nextUrl != null && i < maxPages) {
            response = restTemplate.exchange(nextUrl, HttpMethod.GET, request, IssueSearch[].class);
            issues.addAll(new ArrayList<>(Arrays.asList(response.getBody())));
            nextUrl = getNextPageUrl(response.getHeaders());
            i++;
        }
        return issues;
    }

    private static String getNextPageUrl(HttpHeaders headers) {
        String result = null;

        // If there is no link header, return null
        List<String> linkHeader = headers.get("Link");
        if (linkHeader == null)
            return null;

        // If the header contains no links, return null
        String links = linkHeader.get(0);
        if (links == null || links.isEmpty())
            return null;

        // Return the next page URL or null if none.
        for (String token : links.split(", ")) {
            if (token.endsWith("rel=\"next\"")) {
                // Found the next page. This should look something like
                // <https://api.github.com/repos?page=3&per_page=100>; rel="next"
                int idx = token.indexOf('>');
                result = token.substring(1, idx);
                break;
            }
        }

        return result;
    }
}
