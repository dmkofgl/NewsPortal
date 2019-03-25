package dl.news.portal.domain.repository.mongo.listener;

import dl.news.portal.domain.entity.mongo.Client;
import dl.news.portal.domain.entity.mongo.Community;
import dl.news.portal.domain.repository.mongo.ClientRepository;
import dl.news.portal.domain.repository.mongo.CommunityRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;

@Component
public class MongoClientListener extends AbstractMongoEventListener<Client> {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private CommunityRepository communityRepository;

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Client> event) {
        Client client = clientRepository.findById((ObjectId) event.getSource().get("_id")).get();
        if (client.getCompany() != null) {
            removeOwnedCommunity(client);
        }
        if (client.getCommunities() != null) {
            cleanCommunities(client);
        }
    }

    private void cleanCommunities(Client client) {
        client.getCommunities().forEach(comm -> this.leaveCommunity(client, comm));
    }

    private void removeOwnedCommunity(Client client) {
        communityRepository.delete(client.getCompany());
    }


    private void leaveCommunity(Client client, Community community) {
        community.getClients().remove(client);
        communityRepository.save(community);
    }
}
