package dl.news.portal.domain.dto;

import dl.news.portal.domain.entity.News;
import dl.news.portal.utils.ValidationMode;

import javax.validation.constraints.NotBlank;

public class NewsDto {
    @NotBlank(groups = ValidationMode.Create.class)
    private String title;
    @NotBlank(groups = ValidationMode.Create.class)
    private String content;

    public NewsDto(News news) {
        this(news.getTitle(), news.getContent());
    }

    public NewsDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
