package dl.news.portal.domain.dto;

import java.util.Date;

public class NewsDtoBuilder {
    private NewsDto dto;

    public NewsDtoBuilder() {
        dto = new NewsDto();
    }

    public NewsDto build() {
        return dto;
    }

    public NewsDtoBuilder withTitle(String title) {
        dto.setTitle(title);
        return this;
    }

    public NewsDtoBuilder withContent(String content) {
        dto.setContent(content);
        return this;
    }

    public NewsDtoBuilder withStartCreatedDate(Date createdDate) {
        dto.setCreateDate(createdDate);
        return this;
    }

    public NewsDtoBuilder withEndCreatedDate(Date createdDate) {
        dto.setEndCreateDate(createdDate);
        return this;
    }
}