package dl.news.portal.domain.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import dl.news.portal.domain.entity.User;
import dl.news.portal.web.controller.UserController;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class UserResource extends ResourceSupport {
    public User getUser() {
        return user;
    }

    private final User user;

    public UserResource(@JsonProperty("content") User user) {
        this.user = user;
        final Long id = user.getId();
        add(linkTo(methodOn(UserController.class).getUserById(id)).withSelfRel());

    }
}
