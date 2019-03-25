package dl.news.portal.domain.service;

import dl.news.portal.domain.entity.mongo.Client;
import dl.news.portal.domain.entity.mongo.Community;
import org.springframework.stereotype.Service;

@Service
public interface SubscribeClientService {

    void subscribeClientToCommunity(Client client, Community community);
}
