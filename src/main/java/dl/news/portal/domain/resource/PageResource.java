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
        addPageNumberLinkWithRel(0, Link.REL_FIRST);
        int lastPageNumber = page.getTotalPages() <= 0 ? 0 : page.getTotalPages() - 1;
        addPageNumberLinkWithRel(lastPageNumber, Link.REL_LAST);
        if (page.hasNext()) {
            addPageNumberLinkWithRel(page.nextPageable().getPageNumber(), Link.REL_NEXT);
        }
        if (page.hasPrevious()) {
            addPageNumberLinkWithRel(page.previousPageable().getPageNumber(), Link.REL_PREVIOUS);
        }

    }

    private void addSelfLink() {
        String path = getCurrentRequestUriBuilder().build().toString();
        Link link = new Link(path, Link.REL_SELF);
        add(link);
    }

    private void addFirstLink() {
        String path = getCurrentRequestUriBuilder().replaceQueryParam("page", "0").build().toString();
        Link link = new Link(path, Link.REL_FIRST);
        add(link);
    }

    private void addLastLink() {
        Integer lastPageNumber = page.getTotalPages() - 1;
        String path = getCurrentRequestUriBuilder().replaceQueryParam("page", lastPageNumber).build().toString();
        Link link = new Link(path, Link.REL_FIRST);
        add(link);
    }

    private void addNextLink() {
        Integer number = page.nextPageable().getPageNumber();
        String path = getCurrentRequestUriBuilder().replaceQueryParam("page", number).build().toString();
        Link link = new Link(path, Link.REL_FIRST);
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
