package dl.news.portal.domain.service;

import dl.news.portal.domain.entity.mongo.Client;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ClientServiceTest {
    @Autowired
    private ClientService clientService;
    @Autowired
    private MongoOperations ops;

    @After
    public void dropCollection() {
        this.ops.getCollection("client").drop();
    }

    @Test
    public void createClient_whenDataCorrect_shouldChangeCount() {
        final Long count = clientService.count();
        Client client = createClient();

        clientService.save(client);
        assertNotEquals(count, clientService.count());
    }

    @Test
    public void deleteClient_whenDataCorrect_shouldNotFound() {
        Client client = createClient();
        ops.getCollection("client").insertOne(createClientBson(client));
        clientService.delete(client);
        assertFalse(clientService.findById(client.getId()).isPresent());
    }


    private Client createClient() {
        Client client = new Client();
        client.setId(ObjectId.get());
        client.setClientName("name");
        return client;
    }

    private Document createClientBson(Client client) {
        Document document = new Document();
        document.append("id", client.getId());
        document.append("name", client.getClientName());
        document.append("communities", client.getCommunities());
        return document;
    }
}