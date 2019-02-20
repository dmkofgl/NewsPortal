package dl.news.portal.domain.dto;

import dl.news.portal.domain.entity.News;
import dl.news.portal.utils.ValidationMode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.util.Date;

public class NewsDto {

    @NotBlank(groups = ValidationMode.Create.class)
    private String title;
    @NotBlank(groups = ValidationMode.Create.class)
    private String content;

    private Date createDate;
    private Date endCreateDate;


    public NewsDto(News news) {
        this(news.getTitle(), news.getContent());
    }

    public NewsDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public NewsDto() {
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

    public Date getEndCreateDate() {
        return endCreateDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setEndCreateDate(Date endCreateDate) {
        this.endCreateDate = endCreateDate;
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public void setCreateDate(Date date) {
        this.createDate = date;
    }
}
