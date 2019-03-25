package dl.news.portal.domain.service;

import dl.news.portal.domain.entity.mongo.Community;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommunityServiceTest {
    @Autowired
    private CommunityService communityService;
    @Autowired
    private MongoOperations ops;

    @After
    public void dropCollection() {
        this.ops.getCollection("community").drop();
    }
    @Test
    public void createCommunity() {
        final Long count = communityService.count();
        Community community = new Community();
        community.setName("comm");
        communityService.save(community);

        assertNotEquals(count, communityService.count());
    }
}