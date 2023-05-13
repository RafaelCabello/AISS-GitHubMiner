package aiss.githubminer.service;

import aiss.githubminer.model.IssueSearch;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class IssueServiceTest {

    @Autowired
    IssueService service;
    @Test
    @DisplayName("Get issues owner spring-projects, repoName spring-framework")
    void findIssues() {
        // List<IssueSearch> issues = service.findAllIssues("spring-projects", "spring-framework");
        List<IssueSearch> issues = service.findAllIssues("spring-projects", "spring-framework");
        assertTrue(!issues.isEmpty(), "No issues");
        issues.forEach(c-> {
            System.out.println(c);
            System.out.println("***********************************************************************************");
        });
    }
}