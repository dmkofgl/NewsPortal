package dl.news.portal.domain.response;

import dl.news.portal.domain.entity.UserProfile;
import dl.news.portal.utils.HateoasLink;
import dl.news.portal.web.controller.UserController;
import io.swagger.models.HttpMethod;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class UserResponse extends ResourceSupport {
    private final UserProfile user;

    public UserResponse(UserProfile user) {
        this.user = user;
        final Long id = user.getId();
        add(linkTo(methodOn(UserController.class).getUserById(id)).withSelfRel());
        add(new HateoasLink(linkTo(methodOn(UserController.class).deleteUser(id)).withRel("delete"), HttpMethod.DELETE));
        add(new HateoasLink(linkTo(methodOn(UserController.class).updateUser(id, null)).withRel("update"), HttpMethod.PATCH));
    }

    public String getUsername() {
        return user.getUsername();
    }


}
