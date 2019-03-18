package dl.news.portal.domain.repository;

import dl.news.portal.domain.entity.OauthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OauthUserRepository extends JpaRepository<OauthUser, Long>, JpaSpecificationExecutor<OauthUser> {
        }
