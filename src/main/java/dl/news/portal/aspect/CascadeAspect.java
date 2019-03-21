package dl.news.portal.aspect;

import dl.news.portal.domain.entity.mongo.Client;
import dl.news.portal.domain.entity.mongo.Community;
import dl.news.portal.domain.entity.mongo.Post;
import dl.news.portal.domain.service.ClientService;
import dl.news.portal.domain.service.CommunityService;
import dl.news.portal.domain.service.PostService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Aspect
@Component
public class CascadeAspect {
    @Autowired
    private CommunityService communityService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private PostService postService;

    @Pointcut("execution(* dl.news.portal.domain.service.ClientService.delete(..))")
    public void deleteClient() {
    }

    @Pointcut("execution(* dl.news.portal.domain.service.CommunityService.create(..))")
    public void createCommunity() {
    }

    @Pointcut("execution(* dl.news.portal.domain.service.PostService.publishPost(..))")
    public void createPost() {
    }

    @Pointcut("execution(* dl.news.portal.domain.service.PostService.delete(..))")
    public void deletePost() {
    }

    @Pointcut("execution(* dl.news.portal.domain.service.CommunityService.delete(..))")
    public void deleteCommunity() {
    }

    @AfterReturning(pointcut = "deleteCommunity()")
    public void afterDeleteCommunity(JoinPoint joinPoint) throws Throwable {
        Object[] objects = joinPoint.getArgs();
        Community community = (Community) objects[0];
        releaseCommunityClients(community);
        if (community.getOwner() != null) {
            releaseCompanyOwner(community);
        }
    }

    private void releaseCommunityClients(Community community) {
        community.getClients().forEach(
                client -> {
                    client.getCommunities().remove(community);
                    clientService.create(client);
                }
        );
    }

    private void releaseCompanyOwner(Community community) {
        Client client = community.getOwner();
        client.setCompany(null);
        clientService.create(client);
    }

    @AfterReturning(pointcut = "createCommunity()")
    public void afterCreateCommunity(JoinPoint joinPoint) throws Throwable {
        Object[] objects = joinPoint.getArgs();
        Community community = (Community) objects[0];
        if (community.getOwner() != null) {
            setCommunityOwner(community);
        }
    }

    private void setCommunityOwner(Community community) {
        Client client = community.getOwner();
        client.setCompany(community);
        clientService.create(client);

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
        communityService.create(community);
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
        communityService.create(community);
    }

    @AfterReturning(pointcut = "deleteClient()")
    public void afterDeleteClient(JoinPoint joinPoint) throws Throwable {
        Object[] objects = joinPoint.getArgs();
        Client client = (Client) objects[0];
        leaveCommunities(client);
        if (client.getCompany() != null) {
            removeCompany(client);
        }
    }

    private void leaveCommunities(Client client) {
        List<Community> communities = client.getCommunities();
        communities.forEach(c -> {
            c.getClients().remove(client);
            communityService.create(c);
        });
    }

    private void removeCompany(Client client) {
        communityService.delete(client.getCompany());
    }

}
