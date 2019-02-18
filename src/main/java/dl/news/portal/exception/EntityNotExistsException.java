package dl.news.portal.exception;

import javax.persistence.EntityNotFoundException;

public class EntityNotExistsException extends EntityNotFoundException {
    private String entityName;

    public EntityNotExistsException(String entityName, String message) {
        super(message);
        this.entityName = entityName;
    }

    public EntityNotExistsException(String entityName) {
        this(entityName, "empty");
    }

    public String getEntityName() {
        return entityName;
    }

}
