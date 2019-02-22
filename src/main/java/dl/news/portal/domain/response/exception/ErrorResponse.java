package dl.news.portal.domain.response.exception;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class ErrorResponse extends ResourceSupport {
    private String message;
    private String code;

    public ErrorResponse(String message, String code) {
        this.message = message;
        this.code = code;
        addSelfLink();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    private void addSelfLink() {
        String path = ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString();
        Link link = new Link(path, Link.REL_SELF);
        add(link);
    }

}
