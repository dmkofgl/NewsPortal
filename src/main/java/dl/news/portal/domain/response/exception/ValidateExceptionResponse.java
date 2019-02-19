package dl.news.portal.domain.response.exception;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.ConstraintViolation;

public class ValidateExceptionResponse extends ResourceSupport {
    private ConstraintViolation constraintViolation;

    public ValidateExceptionResponse(ConstraintViolation constraintViolation) {
        this.constraintViolation = constraintViolation;
        addSelfLink();
    }

    public String getPath() {
        return constraintViolation.getPropertyPath().toString();
    }

    public String getMessage() {
        return constraintViolation.getMessage();
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
