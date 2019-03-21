package dl.news.portal.domain.entity;

import org.springframework.data.mongodb.core.mapping.DBRef;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;

public class Editor {
    @Id
    private String id;
    @OneToOne
    @JoinColumn
    @DBRef
    private UserProfile userProfile;
    @OneToMany
    private List<News> editedNews;

    {
        editedNews = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public List<News> getEditedNews() {
        return editedNews;
    }

    public void setEditedNews(List<News> editedNews) {
        this.editedNews = editedNews;
    }
}
