package aiss.githubminer.service;

import aiss.githubminer.model.UserUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserService {
    @Autowired
    RestTemplate restTemplate;

    public UserUnit findUser(String username) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + "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");
        HttpEntity<UserUnit> request = new HttpEntity<UserUnit>(headers);

        String uri = "https://api.github.com/users/" + username;
        ResponseEntity<UserUnit> response = restTemplate.exchange(uri, HttpMethod.GET, request, UserUnit.class);
        UserUnit userSearch = response.getBody();

        return userSearch;
    }
}
