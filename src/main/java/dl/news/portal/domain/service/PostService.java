package dl.news.portal.domain.service;

import dl.news.portal.domain.entity.mongo.Post;
import org.bson.types.ObjectId;

import java.util.Optional;

public interface PostService {

    void publishPost(Post post);

    void delete(Post post);

    Optional<Post> findById(ObjectId id);

    Long count();
}
