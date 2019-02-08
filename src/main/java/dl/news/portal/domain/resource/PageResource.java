package dl.news.portal.domain.resource;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class PageResource<T> extends PagedResources<T> {


    public PageResource(Page<T> page) {
        super(page.getContent(), new PageMetadata(page.getSize(), page.getNumber(), page.getTotalElements(), page.getTotalPages()));
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
