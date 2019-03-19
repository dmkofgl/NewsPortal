package dl.news.portal.domain.repository;

import dl.news.portal.domain.entity.User;
import dl.news.portal.domain.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUserProfileUsername(String username);
}
