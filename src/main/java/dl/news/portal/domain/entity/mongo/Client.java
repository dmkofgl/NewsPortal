package dl.news.portal.domain.entity.mongo;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Document
public class Client {
    @Id
    private ObjectId id;
    private String clientName;
    @DBRef(lazy = true)
    private List<Community> communities;
    private Community company;

    {
        communities = new ArrayList<>();
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public List<Community> getCommunities() {
        return communities;
    }

    public void setCommunities(List<Community> communities) {
        this.communities = communities;
    }

    public Community getCompany() {
        return company;
    }

    public void setCompany(Community company) {
        this.company = company;
    }

    @Override
    public boolean equals(Object obj) {
        Client client = (Client) obj;
        return this.getId().equals(client.getId());
    }
}
