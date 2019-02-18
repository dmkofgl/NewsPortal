package dl.news.portal.domain.dto;

import dl.news.portal.domain.entity.News;

import javax.validation.constraints.NotBlank;
import java.util.Optional;

public class NewsDto {
    private Optional<@NotBlank String> title;
    private Optional<@NotBlank String> content;

    public NewsDto(String title, String content) {
        this.title = Optional.ofNullable(title);
        this.content = Optional.ofNullable(content);
    }

    public NewsDto(News news) {
        this(news.getTitle(), news.getContent());
    }

    public NewsDto() {
        this(null, null);
    }

    public Optional<String> getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = Optional.ofNullable(title);
    }

    public Optional<String> getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = Optional.ofNullable(content);
    }

    public void transfer(News receiver) {
        title.ifPresent(receiver::setTitle);
        content.ifPresent(receiver::setContent);
    }

    public static News of(NewsDto newsDto) {
        News news = new News();
        newsDto.transfer(news);
        return news;
    }
}
