package dl.news.portal.domain.repository.mongo.listener;

import dl.news.portal.domain.entity.mongo.Client;
import dl.news.portal.domain.entity.mongo.Community;
import dl.news.portal.domain.repository.mongo.ClientRepository;
import dl.news.portal.domain.repository.mongo.CommunityRepository;
import dl.news.portal.domain.repository.mongo.PostRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MongoCommunityListener extends AbstractMongoEventListener<Community> {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private CommunityRepository communityRepository;
    @Autowired
    private PostRepository postRepository;

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Community> event) {
        Community community = communityRepository.findById((ObjectId) event.getSource().get("_id")).get();
        releaseCommunityPosts(community);
        if (community.getOwner() != null) {
            removeOwnedCommunity(community);
        }
        if (community.getClients() != null) {
            cleanCommunities(community);
        }
    }

    private void cleanCommunities(Community community) {
        community.getClients().forEach(client -> this.leaveCommunity(client, community));
    }

    private void removeOwnedCommunity(Community community) {
        community.getOwner().setCompany(null);
        clientRepository.save(community.getOwner());
    }

    private void leaveCommunity(Client client, Community community) {
        List<Community> communities = client.getCommunities();
        communities.remove(community);
        clientRepository.save(client);
    }

    private void releaseCommunityPosts(Community community) {
        community.getPosts().forEach(post -> postRepository.delete(post));
    }
}
