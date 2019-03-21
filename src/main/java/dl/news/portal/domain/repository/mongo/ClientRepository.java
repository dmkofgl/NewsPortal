package dl.news.portal.domain.repository.mongo;

import dl.news.portal.domain.entity.mongo.Client;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClientRepository extends MongoRepository<Client, ObjectId> {
}
