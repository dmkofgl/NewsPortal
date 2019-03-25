package dl.news.portal.domain.service;

import dl.news.portal.domain.entity.mongo.Community;
import org.bson.types.ObjectId;

import java.util.Optional;

public interface CommunityService {

    void save(Community community);

    void delete(Community community);

    Optional<Community> findById(ObjectId id);

    Long count();
}
