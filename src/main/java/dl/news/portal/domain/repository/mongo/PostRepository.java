package dl.news.portal.domain.repository.mongo;

import dl.news.portal.domain.entity.mongo.Post;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepository extends MongoRepository<Post, ObjectId> {
}
