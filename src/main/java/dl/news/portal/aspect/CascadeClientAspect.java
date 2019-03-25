package dl.news.portal.aspect;

import dl.news.portal.domain.entity.mongo.Client;
import dl.news.portal.domain.entity.mongo.Community;
import dl.news.portal.domain.service.CommunityService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Aspect
@Component
@Profile("aspect-cascade")
public class CascadeClientAspect {
    @Autowired
    private CommunityService communityService;

    @Pointcut("execution(* dl.news.portal.domain.repository.mongo.ClientRepository.delete(..))")
    public void deleteClient() {
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
            communityService.save(c);
        });
    }

    private void removeCompany(Client client) {
        communityService.delete(client.getCompany());
    }

}
