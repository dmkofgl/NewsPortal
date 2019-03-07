package dl.news.portal.domain.repository;

import dl.news.portal.domain.entity.RefreshToken;
import dl.news.portal.domain.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
    List<RefreshToken> findAllByOwner(User user);

}
