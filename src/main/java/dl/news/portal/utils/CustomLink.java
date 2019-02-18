package dl.news.portal.utils;

import io.swagger.models.HttpMethod;
import org.springframework.hateoas.Link;

public class CustomLink extends Link {

    private HttpMethod method;

    public CustomLink(Link link, HttpMethod method) {
        super(link.getHref(), link.getRel());
        this.method = method;
    }

    public HttpMethod getMethod() {
        return method;
    }


}
