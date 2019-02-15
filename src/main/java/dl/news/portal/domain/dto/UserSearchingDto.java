package dl.news.portal.domain.dto;

import dl.news.portal.domain.entity.User;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

public class UserSearchingDto implements DtoTransfer<User>, DtoTransoform<User>, SearchingSpecification<User> {

    private Optional<String> username;

    private Optional<String> email;

    public void setUsername(String username) {
        this.username = Optional.ofNullable(username);
    }

    public void setEmail(String email) {
        this.email = Optional.ofNullable(email);
    }

    public UserSearchingDto(String username, String email) {
        this.username = Optional.ofNullable(username);
        this.email = Optional.ofNullable(email);
    }

    public UserSearchingDto() {
        this(null, null);
    }

    @Override
    public void transfer(User receiver) {
        username.ifPresent(receiver::setUsername);
        email.ifPresent(receiver::setEmail);
    }

    @Override
    public User transform() {
        User user = new User();
        transfer(user);
        return user;
    }

    public Specification<User> getSpecification() {
        return (root, criteriaQuery, criteriaBuilder) -> {
            Collection<Predicate> predicates = new HashSet<>();
            username.ifPresent(u -> predicates.add(criteriaBuilder.like(root.get("username"), "%" + u + "%")));
            email.ifPresent(u -> predicates.add(criteriaBuilder.like(root.get("email"), "%" + u + "%")));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
