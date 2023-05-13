package aiss.githubminer.service;

import aiss.githubminer.model.CommentSearch;
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

    public List<CommentSearch> findByProjectIssue(String owner, String repoName, String issueNumber, Integer maxPages, String token){
        String uri = "https://api.github.com/repos/" + owner + "/" + repoName + "/issues/" + issueNumber + "/comments";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<CommentSearch[]> request = new HttpEntity<>(null, headers);

        ResponseEntity<CommentSearch[]> response = restTemplate.exchange(uri, HttpMethod.GET, request, CommentSearch[].class);

        List<CommentSearch> comments = new ArrayList<>(Arrays.asList(response.getBody()));

        String nextUrl = getNextPageUrl(response.getHeaders());
        int i = 1;
        while (nextUrl != null && i < maxPages) {
            response = restTemplate.exchange(nextUrl, HttpMethod.GET, request, CommentSearch[].class);
            comments.addAll(new ArrayList<>(Arrays.asList(response.getBody())));
            nextUrl = getNextPageUrl(response.getHeaders());
            i++;
        }
        return comments;
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
