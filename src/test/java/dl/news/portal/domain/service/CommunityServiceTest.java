package dl.news.portal.domain.service;

import com.mongodb.client.model.IndexOptions;
import dl.news.portal.domain.entity.mongo.Community;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommunityServiceTest {
    @Autowired
    private CommunityService communityService;
    @Autowired
    private MongoTemplate ops;

    @Before
    public void setUp() throws Exception {
        this.ops.getDb().drop();

        Document document = new Document();
        document.append("owner", 1);
        IndexOptions indexOptions = new IndexOptions();
        indexOptions.unique(true);

        this.ops.createCollection("client");
        this.ops.createCollection("community").createIndex(document, indexOptions);

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