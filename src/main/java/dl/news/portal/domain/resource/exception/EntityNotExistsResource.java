package dl.news.portal.domain.resource.exception;

import dl.news.portal.exception.EntityNotExistsException;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class EntityNotExistsResource extends ResourceSupport {
    private EntityNotExistsException exception;

    public String getEntityName() {
        return exception.getEntityName();
    }

    public String getMessage() {
        return exception.getMessage();
    }

    public EntityNotExistsResource(EntityNotExistsException exception) {
        this.exception = exception;
        addSelfLink();
    }

    private void addSelfLink() {
        String path = createUri();
        Link link = new Link(path, Link.REL_SELF);
        add(link);
    }

    private String createUri() {
        return ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString();
    }
}
