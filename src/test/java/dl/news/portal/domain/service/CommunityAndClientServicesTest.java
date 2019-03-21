package dl.news.portal.domain.service;

import com.mongodb.client.model.IndexOptions;
import dl.news.portal.domain.entity.mongo.Client;
import dl.news.portal.domain.entity.mongo.Community;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommunityAndClientServicesTest {
    @Autowired
    private SubscribeClientService subscribeClientService;
    @Autowired
    private CommunityService communityService;
    @Autowired
    private ClientService clientService;

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

    @After
    public void dropCollection() {
    }

    @Test
    public void subscribeClientToCommunity_whenDataIsCorrect_shouldAddCommunityInClient() {
        Community community = createCommunity();
        Client client = createClient();

        communityService.save(community);
        clientService.save(client);

        subscribeClientService.subscribeClientToCommunity(client, community);

        Client newClient = clientService.findById(client.getId()).get();

        assertTrue(newClient.getCommunities().stream().anyMatch(c -> c.getId().equals(community.getId())));
    }

    @Test
    public void subscribeClientToCommunity_whenDataIsCorrect_shouldAddClientInCommunity() {
        Community community = createCommunity();
        Client client = createClient();

        communityService.save(community);
        clientService.save(client);

        subscribeClientService.subscribeClientToCommunity(client, community);

        Community newCommunity = communityService.findById(community.getId()).get();

        assertTrue(newCommunity.getClients().stream().anyMatch(c -> c.getId().equals(client.getId())));
    }


    @Test
    public void createCommunity_whenOwnerExist_shouldOwnerEqualClient() {
        Client client = createClient();
        Community community = createCommunity();
        community.setOwner(client);

        clientService.save(client);
        communityService.save(community);

        Community newCommunity = communityService.findById(community.getId()).get();
        assertEquals(client.getId(), newCommunity.getOwner().getId());
    }

    @Test(expected = DuplicateKeyException.class)
    public void createCommunity_whenOwnerHasSomeCommunity_shouldThrowException() {
        Client client = createClient();
        Community community = createCommunity();
        Community communitySecond = createCommunity();
        community.setOwner(client);
        communitySecond.setOwner(client);

        clientService.save(client);
        communityService.save(community);
        communityService.save(communitySecond);
    }

    @Test
    public void deleteClient_whenIsCommunityOwner_shouldDeleteCommunity() {
        Client client = createClient();
        Community community = createCommunity();
        community.setOwner(client);

        clientService.save(client);
        communityService.save(community);
        clientService.delete(client);

        assertFalse(communityService.findById(community.getId()).isPresent());
    }


    private Community createCommunity() {
        Community community = new Community();
        community.setName("comm");
        return community;
    }

    private Client createClient() {
        Client client = new Client();
        client.setClientName("name");
        return client;
    }
}
