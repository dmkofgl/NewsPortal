package dl.news.portal.domain.service.impl;

import dl.news.portal.domain.entity.mongo.Community;
import dl.news.portal.domain.repository.mongo.CommunityRepository;
import dl.news.portal.domain.service.CommunityService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommunityServiceImpl implements CommunityService {
    @Autowired
    private CommunityRepository communityRepository;

    @Override
    public void save(Community community) {
        communityRepository.save(community);
    }

    @Override
    public void delete(Community community) {
        communityRepository.delete(community);
    }

    @Override
    public Optional<Community> findById(ObjectId id) {
        return communityRepository.findById(id);
    }

    @Override
    public Long count() {
        return communityRepository.count();
    }
}
