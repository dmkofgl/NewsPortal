package dl.news.portal.domain.service;

import dl.news.portal.domain.entity.mongo.Client;
import dl.news.portal.domain.entity.mongo.Community;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoOperations;
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
    private MongoOperations ops;

    @After
    public void dropCollection() {
        this.ops.getCollection("client").drop();
        this.ops.getCollection("community").drop();
    }

    @Test
    public void subscribeClientToCommunity_whenDataIsCorrect_shouldAddCommunityInClient() {
        Community community = createCommunity();
        Client client = createClient();

        communityService.create(community);
        clientService.create(client);

        subscribeClientService.subscribeClientToCommunity(client, community);

        Client newClient = clientService.findById(client.getId()).get();

        assertTrue(newClient.getCommunities().stream().anyMatch(c -> c.getId().equals(community.getId())));
    }
    @Test
    public void subscribeClientToCommunity_whenDataIsCorrect_shouldAddClientInCommunity() {
        Community community = createCommunity();
        Client client = createClient();

        communityService.create(community);
        clientService.create(client);

        subscribeClientService.subscribeClientToCommunity(client, community);

        Community newCommunity = communityService.findById(community.getId()).get();

        assertTrue(newCommunity.getClients().stream().anyMatch(c -> c.getId().equals(client.getId())));
    }


    @Test
    public void createCommunity_whenOwnerExist_shouldOwnerEqualClient() {
        Client client = createClient();
        Community community = createCommunity();
        community.setOwner(client);

        clientService.create(client);
        communityService.create(community);

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

        clientService.create(client);
        communityService.create(community);
        communityService.create(communitySecond);
    }

    @Test
    public void deleteClient_whenIsCommunityOwner_shouldDeleteCommunity() {
        Client client = createClient();
        Community community = createCommunity();
        community.setOwner(client);

        clientService.create(client);
        communityService.create(community);
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
