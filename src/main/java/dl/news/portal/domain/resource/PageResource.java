package dl.news.portal.domain.resource;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class PageResource<T> extends PagedResources<T> {
    private Page<T> page;

    public PageResource(Page<T> page) {
        super(page.getContent(), new PageMetadata(page.getSize(), page.getNumber(), page.getTotalElements(), page.getTotalPages()));
        this.page = page;
        addSelfLink();
        addPageableLinks();
    }

    private void addPageableLinks() {
        int firstPageNumber = 0;
        int lastPageNumber = page.getTotalPages() <= 0 ? 0 : page.getTotalPages() - 1;
        int nextPageNumber = page.nextPageable().getPageNumber();
        int previousPageNumber = page.previousPageable().getPageNumber();
        addPageNumberLinkWithRel(firstPageNumber, Link.REL_FIRST);
        addPageNumberLinkWithRel(lastPageNumber, Link.REL_LAST);
        if (page.hasNext()) {
            addPageNumberLinkWithRel(nextPageNumber, Link.REL_NEXT);
        }
        if (page.hasPrevious()) {
            addPageNumberLinkWithRel(previousPageNumber, Link.REL_PREVIOUS);
        }
    }

    private void addSelfLink() {
        String path = getCurrentRequestUriBuilder().build().toString();
        Link link = new Link(path, Link.REL_SELF);
        add(link);
    }

    private void addPageNumberLinkWithRel(Integer number, String rel) {
        String path = getCurrentRequestUriBuilder().replaceQueryParam("page", number).build().toString();
        Link link = new Link(path, rel);
        add(link);
    }

    private ServletUriComponentsBuilder getCurrentRequestUriBuilder() {
        return ServletUriComponentsBuilder.fromCurrentRequest();
    }

}
