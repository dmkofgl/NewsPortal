package dl.news.portal.domain.resource;

import dl.news.portal.domain.entity.News;
import dl.news.portal.utils.CustomLink;
import dl.news.portal.web.controller.NewsController;
import dl.news.portal.web.controller.UserController;
import io.swagger.models.HttpMethod;
import org.springframework.hateoas.ResourceSupport;

import java.util.Date;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class NewsResource extends ResourceSupport {
    private News news;

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


    public NewsResource(News news) {
        this.news = news;
        final Long id = news.getId();
        add(linkTo(methodOn(NewsController.class).getNewsById(id)).withSelfRel());
        add(new CustomLink(linkTo(methodOn(NewsController.class).deleteNews(id)).withRel("delete"), HttpMethod.DELETE));
        add(new CustomLink(linkTo(methodOn(NewsController.class).updateNews(id, null)).withRel("update"), HttpMethod.PATCH));
        add(linkTo(methodOn(UserController.class).getUserById(news.getAuthor().getId())).withRel("author"));
    }
}
