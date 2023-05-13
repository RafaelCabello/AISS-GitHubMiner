package aiss.githubminer.service;

import aiss.githubminer.model.CommentSearch;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommentServiceTest {

    @Autowired
    CommentService service;
    @Test
    @DisplayName("Get comments issue 30405, owner spring-projects, repoName spring-framework")
    void findIssues() {
        List<CommentSearch> comments = service.findComments("spring-projects", "spring-framework", "30405");
        assertTrue(!comments.isEmpty(), "No comments");
        comments.forEach(c-> {
            System.out.println(c);
            System.out.println("***********************************************************************************");
        });
    }
}