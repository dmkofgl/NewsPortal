package dl.news.portal.utils;

import io.swagger.models.HttpMethod;
import org.springframework.hateoas.Link;

public class HateoasLink extends Link {

    private HttpMethod method;

    public HateoasLink(Link link, HttpMethod method) {
        super(link.getHref(), link.getRel());
        this.method = method;
    }

    public HttpMethod getMethod() {
        return method;
    }


}
