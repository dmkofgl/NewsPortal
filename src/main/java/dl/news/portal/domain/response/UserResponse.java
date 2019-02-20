package dl.news.portal.domain.response;

import dl.news.portal.domain.entity.User;
import dl.news.portal.utils.HateoasLink;
import dl.news.portal.web.controller.UserController;
import io.swagger.models.HttpMethod;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class UserResponse extends ResourceSupport {
    private final User user;

    public UserResponse(User user) {
        this.user = user;
        final Long id = user.getId();
        add(linkTo(methodOn(UserController.class).getUserById(id)).withSelfRel());
        add(new HateoasLink(linkTo(methodOn(UserController.class).deleteUser(id)).withRel("delete"), HttpMethod.DELETE));
        add(new HateoasLink(linkTo(methodOn(UserController.class).updateUser(id, null)).withRel("update"), HttpMethod.PATCH));
    }

    public String getUsername() {
        return user.getUsername();
    }

    public String getEmail() {
        return user.getEmail();
    }


}
