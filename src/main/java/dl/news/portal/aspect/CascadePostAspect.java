package dl.news.portal.aspect;

import dl.news.portal.domain.entity.mongo.Community;
import dl.news.portal.domain.entity.mongo.Post;
import dl.news.portal.domain.service.CommunityService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Aspect
@Component
public class CascadePostAspect {
    @Autowired
    private CommunityService communityService;


    @Pointcut("execution(* dl.news.portal.domain.service.PostService.publishPost(..))")
    public void createPost() {
    }

    @Pointcut("execution(* dl.news.portal.domain.service.PostService.delete(..))")
    public void deletePost() {
    }


    @AfterReturning(pointcut = "createPost()")
    public void afterCreatePost(JoinPoint joinPoint) throws Throwable {
        Object[] objects = joinPoint.getArgs();
        Post post = (Post) objects[0];
        if (post.getPublisher() != null) {
            publishPostInCommunity(post);
        }
    }

    private void publishPostInCommunity(Post post) {
        Community community = communityService.findById(post.getPublisher().getId()).get();
        List<Post> posts = community.getPosts();
        posts.add(post);
        community.setPosts(posts);
        communityService.save(community);
    }

    @AfterReturning(pointcut = "deletePost()")
    public void afterDeletePost(JoinPoint joinPoint) throws Throwable {
        Object[] objects = joinPoint.getArgs();
        Post post = (Post) objects[0];
        if (post.getPublisher() != null) {
            releasePostInCommunity(post);
        }
    }

    private void releasePostInCommunity(Post post) {
        Community community = communityService.findById(post.getPublisher().getId()).get();
        List<Post> posts = community.getPosts();
        posts.remove(post);
        community.setPosts(posts);
        communityService.save(community);
    }


}
