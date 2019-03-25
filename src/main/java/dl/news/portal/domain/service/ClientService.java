package dl.news.portal.domain.service;

import dl.news.portal.domain.entity.mongo.Client;
import org.bson.types.ObjectId;

import java.util.Optional;

public interface ClientService {

    void save(Client client);

    void delete(Client client);

    Optional<Client> findById(ObjectId id);

    Long count();
}
