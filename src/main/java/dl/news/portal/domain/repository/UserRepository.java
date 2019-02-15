package dl.news.portal.domain.repository;

import dl.news.portal.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    List<User> findByUsernameIgnoreCaseContaining(String username);

    List<User> findByUsernameContaining(String username);
}
