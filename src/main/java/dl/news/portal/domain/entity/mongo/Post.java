package dl.news.portal.domain.entity.mongo;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Document
public class Post {
    @Id
    private ObjectId id;
    private String content;
    @DBRef
    private Community publisher;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Community getPublisher() {
        return publisher;
    }

    public void setPublisher(Community publisher) {
        this.publisher = publisher;
    }

    @Override
    public boolean equals(Object obj) {
        Post post = (Post) obj;
        return post.getId().equals(this.getId());
    }
}
