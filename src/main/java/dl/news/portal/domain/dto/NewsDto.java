package dl.news.portal.domain.dto;

import dl.news.portal.domain.entity.News;
import dl.news.portal.domain.entity.User;

import java.util.Optional;

public class NewsDto {
    private Optional<String> title;
    private Optional<String> content;
    private Optional<User> author;

    public NewsDto(String title, String content, User author) {
        this.title = Optional.ofNullable(title);
        this.content = Optional.ofNullable(content);
        this.author = Optional.ofNullable(author);
    }

    public NewsDto() {
        this(null, null, null);
    }

    public void mapToEntity(News news) {
        title.ifPresent(news::setTitle);
        content.ifPresent(news::setContent);
        author.ifPresent(news::setAuthor);

    }
}
