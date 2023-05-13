package aiss.githubminer.service;

import aiss.githubminer.model.ProjectSearch;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProjectServiceTest {

    @Autowired
    ProjectService service;

    @Test
    @DisplayName("Get repository owner RafaelCabello, repoName AISS-GitMiner")
    void findOne() {
        ProjectSearch project = service.findOne("RafaelCabello", "AISS-GitMiner");
        System.out.println(project);
    }
}