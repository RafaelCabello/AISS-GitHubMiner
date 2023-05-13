package aiss.githubminer.service;

import aiss.githubminer.model.CommitSearch;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommitServiceTest {

    @Autowired
    CommitService service;
    @Test
    @DisplayName("Get commits owner RafaelCabello, repoName AISS-GitMiner")
    void findCommits() {
        List<CommitSearch> commits = service.findAllCommits("RafaelCabello", "AISS-GitMiner");
        assertTrue(!commits.isEmpty(), "The list of commits is empty");
        System.out.println(commits);
    }
}