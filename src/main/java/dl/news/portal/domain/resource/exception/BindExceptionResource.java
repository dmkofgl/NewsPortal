package dl.news.portal.domain.resource.exception;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

public class BindExceptionResource extends ResourceSupport {
    class BindObjectErrorResource extends ResourceSupport {
        private FieldError objectError;

        public String getField() {
            return objectError.getField();
        }

        public String getValue() {
            return objectError.getRejectedValue() == null ? "null" : objectError.getRejectedValue().toString();
        }

        public String getMessage() {
            return objectError.getDefaultMessage();
        }

        public BindObjectErrorResource(ObjectError objectError) {
            this.objectError = (FieldError) objectError;
        }
    }

    private BindingResult bindException;

    public List<BindObjectErrorResource> getErrors() {
        return bindException.getAllErrors().stream().map(BindObjectErrorResource::new).collect(Collectors.toList());
    }

    public BindExceptionResource(BindingResult bindException) {
        this.bindException = bindException;
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
