package dl.news.portal.aspect;

import dl.news.portal.domain.entity.mongo.Client;
import dl.news.portal.domain.entity.mongo.Community;
import dl.news.portal.domain.service.ClientService;
import dl.news.portal.domain.service.PostService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CascadeCommunityAspect {
    @Autowired
    private ClientService clientService;
    @Autowired
    private PostService postService;

    @Pointcut("execution(* dl.news.portal.domain.repository.mongo.CommunityRepository.save(..))")
    public void createCommunity() {
    }

    @Pointcut("execution(* dl.news.portal.domain.repository.mongo.CommunityRepository.delete(..))")
    public void deleteCommunity() {
    }

    @AfterReturning(pointcut = "deleteCommunity()")
    public void afterDeleteCommunity(JoinPoint joinPoint) throws Throwable {
        Object[] objects = joinPoint.getArgs();
        Community community = (Community) objects[0];
        releaseCommunityClients(community);
        releaseCommunityPosts(community);
        if (community.getOwner() != null) {
            releaseCompanyOwner(community);
        }
    }

    private void releaseCommunityClients(Community community) {
        community.getClients().forEach(
                client -> {
                    client.getCommunities().remove(community);
                    clientService.save(client);
                }
        );
    }

    private void releaseCommunityPosts(Community community) {
        community.getPosts().forEach(post -> postService.delete(post));
    }

    private void releaseCompanyOwner(Community community) {
        Client client = community.getOwner();
        client.setCompany(null);
        clientService.save(client);
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
        clientService.save(client);
    }
}
