package dl.news.portal.domain.service;

import dl.news.portal.domain.entity.mongo.Post;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostServiceTest {
    @Autowired
    private MongoOperations ops;

    @After
    public void dropCollection() {
        this.ops.getCollection("post").drop();
    }

    @Autowired
    private PostService postService;

    @Test
    public void CreatePost() {
        Long count = postService.count();
        Post post = new Post();
        post.setContent("post");

        postService.publishPost(post);

        assertNotEquals(count, postService.count());
    }
}