package dl.news.portal.domain.service;

import dl.news.portal.domain.entity.mongo.Community;
import dl.news.portal.domain.entity.mongo.Post;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommunityAndPostServicesTest {
    @Autowired
    private CommunityService communityService;

    @Autowired
    private PostService postService;
    @Autowired
    private MongoOperations ops;

    @After
    public void dropCollection() {
        this.ops.getCollection("post").drop();
        this.ops.getCollection("community").drop();
    }

    @Test
    public void createPost_whenDataIsCorrect_shouldAddPostInCommunity() {
        Community community = createCommunity();
        Post post = createPost();
        post.setPublisher(community);

        communityService.create(community);
        postService.publishPost(post);

        List<Post> newPosts = communityService.findById(post.getPublisher().getId()).get().getPosts();
        assertTrue(newPosts.stream().anyMatch(p -> p.getId().equals(post.getId())));
    }

    @Test
    public void createPost_whenDataIsCorrect_shouldPostPublisherIsThisCommunity() {
        Community community = createCommunity();
        Post post = createPost();
        post.setPublisher(community);

        communityService.create(community);
        postService.publishPost(post);

        Post newPost = postService.findById(post.getId()).get();
        assertEquals(community.getId(), newPost.getPublisher().getId());
    }

    @Test
    public void deletePost_whenDataIsCorrect_shouldPostNotExists() {
        Community community = createCommunity();
        Post post = createPost();
        post.setPublisher(community);

        communityService.create(community);
        postService.publishPost(post);

        postService.delete(post);

        assertFalse(postService.findById(post.getId()).isPresent());
    }

    @Test
    public void deletePost_whenDataIsCorrect_shouldDeletePostInCommunity() {
        Community community = createCommunity();
        Post post = createPost();
        post.setPublisher(community);

        communityService.create(community);
        postService.publishPost(post);

        postService.delete(post);

        List<Post> newPosts = communityService.findById(post.getPublisher().getId()).get().getPosts();
        assertFalse(newPosts.stream().anyMatch(p -> p.getId().equals(post.getId())));
    }

    private Community createCommunity() {
        Community community = new Community();
        community.setName("comm");
        return community;
    }

    private Post createPost() {
        Post post = new Post();
        post.setContent("post");
        return post;
    }
}
