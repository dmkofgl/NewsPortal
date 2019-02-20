package dl.news.portal.domain.response;

import dl.news.portal.domain.entity.News;
import dl.news.portal.utils.HateoasLink;
import dl.news.portal.web.controller.NewsController;
import dl.news.portal.web.controller.UserController;
import io.swagger.models.HttpMethod;
import org.springframework.hateoas.ResourceSupport;

import java.util.Date;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class NewsResponse extends ResourceSupport {
    private News news;

    public NewsResponse(News news) {
        this.news = news;
        final Long id = news.getId();
        add(linkTo(methodOn(NewsController.class).getNewsById(id)).withSelfRel());
        add(new HateoasLink(linkTo(methodOn(NewsController.class).deleteNews(id)).withRel("delete"), HttpMethod.DELETE));
        add(new HateoasLink(linkTo(methodOn(NewsController.class).updateNews(id, null)).withRel("update"), HttpMethod.PATCH));
        if (news.getAuthor() != null) {
            add(linkTo(methodOn(UserController.class).getUserById(news.getAuthor().getId())).withRel("author"));
        }
    }

    public String getTitle() {
        return news.getTitle();
    }

    public String getContent() {
        return news.getContent();
    }

    public Date getCreatedDate() {
        return news.getCreatedDate();
    }

    public Date getUpdatedDate() {
        return news.getUpdatedDate();
    }


}
