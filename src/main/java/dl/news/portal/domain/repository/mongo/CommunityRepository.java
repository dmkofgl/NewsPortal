package dl.news.portal.domain.repository.mongo;

import dl.news.portal.domain.entity.mongo.Community;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommunityRepository extends MongoRepository<Community, ObjectId> {
}
