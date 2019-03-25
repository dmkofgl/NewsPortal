package dl.news.portal.domain.repository.mongo.listener;

import dl.news.portal.domain.entity.mongo.Community;
import dl.news.portal.domain.entity.mongo.Post;
import dl.news.portal.domain.repository.mongo.CommunityRepository;
import dl.news.portal.domain.repository.mongo.PostRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MongoPostListener extends AbstractMongoEventListener<Post> {
    @Autowired
    private CommunityRepository communityRepository;
    @Autowired
    private PostRepository postRepository;

    @Override
    public void onAfterSave(AfterSaveEvent<Post> event) {
        Post post = event.getSource();
        if (post.getPublisher() != null) {
            publishPostInCommunity(post);
        }
    }

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Post> event) {
        Post post = postRepository.findById((ObjectId) event.getSource().get("_id")).get();
        releasePostInCommunity(post);
    }

    private void publishPostInCommunity(Post post) {
        Community community = post.getPublisher();
        List<Post> posts = community.getPosts();
        posts.add(post);
        community.setPosts(posts);
        communityRepository.save(community);
    }

    private void releasePostInCommunity(Post post) {
        Community community = post.getPublisher();
        community.getPosts().remove(post);
        communityRepository.save(community);
    }

}
