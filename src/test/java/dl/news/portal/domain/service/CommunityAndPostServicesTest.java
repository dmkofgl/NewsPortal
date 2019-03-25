package dl.news.portal.domain.service;

import com.mongodb.client.model.IndexOptions;
import dl.news.portal.domain.entity.mongo.Community;
import dl.news.portal.domain.entity.mongo.Post;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
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
    private MongoTemplate ops;

    @Before
    public void setUp() throws Exception {
        this.ops.getDb().drop();

        Document document = new Document();
        document.append("owner", 1);
        IndexOptions indexOptions = new IndexOptions();
        indexOptions.unique(true);

        this.ops.createCollection("client");
        this.ops.createCollection("community").createIndex(document, indexOptions);

    }


    @Test
    public void createPost_whenDataIsCorrect_shouldAddPostInCommunity() {
        Community community = createCommunity();
        Post post = createPost();
        post.setPublisher(community);

        communityService.save(community);
        postService.publishPost(post);

        List<Post> newPosts = communityService.findById(post.getPublisher().getId()).get().getPosts();
        assertTrue(newPosts.stream().anyMatch(p -> p.getId().equals(post.getId())));
    }

    @Test
    public void createPost_whenDataIsCorrect_shouldPostPublisherIsThisCommunity() {
        Community community = createCommunity();
        Post post = createPost();
        post.setPublisher(community);

        communityService.save(community);
        postService.publishPost(post);

        Post newPost = postService.findById(post.getId()).get();
        assertEquals(community.getId(), newPost.getPublisher().getId());
    }

    @Test
    public void deletePost_whenDataIsCorrect_shouldPostNotExists() {
        Community community = createCommunity();
        Post post = createPost();
        post.setPublisher(community);

        communityService.save(community);
        postService.publishPost(post);

        postService.delete(post);

        assertFalse(postService.findById(post.getId()).isPresent());
    }

    @Test
    public void deletePost_whenDataIsCorrect_shouldDeletePostInCommunity() {
        Community community = createCommunity();
        Post post = createPost();
        post.setPublisher(community);

        communityService.save(community);
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
