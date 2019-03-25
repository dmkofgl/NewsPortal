package dl.news.portal.domain.service.impl;

import dl.news.portal.domain.entity.mongo.Client;
import dl.news.portal.domain.entity.mongo.Community;
import dl.news.portal.domain.repository.mongo.ClientRepository;
import dl.news.portal.domain.repository.mongo.CommunityRepository;
import dl.news.portal.domain.service.SubscribeClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscribeClientServiceImpl implements SubscribeClientService {
    @Autowired
    private CommunityRepository communityRepository;
    @Autowired
    private ClientRepository clientRepository;


    @Override
    public void subscribeClientToCommunity(Client client, Community community) {
        List<Community> communities = client.getCommunities();
        communities.add(community);
        client.setCommunities(communities);

        community.getClients().add(client);
        clientRepository.save(client);
        communityRepository.save(community);
    }
}
