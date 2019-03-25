package dl.news.portal.domain.service.impl;

import dl.news.portal.domain.entity.mongo.Post;
import dl.news.portal.domain.repository.mongo.PostRepository;
import dl.news.portal.domain.service.PostService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepository postRepository;

    @Override
    public void publishPost(Post post) {
        postRepository.save(post);
    }

    @Override
    public void delete(Post post) {
        postRepository.delete(post);
    }

    @Override
    public Optional<Post> findById(ObjectId id) {
        return postRepository.findById(id);
    }

    @Override
    public Long count() {
        return postRepository.count();
    }
}
