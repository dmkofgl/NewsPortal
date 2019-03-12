package dl.news.portal.domain.service;

import dl.news.portal.domain.entity.RefreshToken;
import org.springframework.security.core.Authentication;

import java.util.Optional;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(Authentication authentication);

    Optional<RefreshToken> getById(Long id);

    void saveRefreshToken(RefreshToken token);

    String buildToken(RefreshToken token);

    String takeRefreshToken(Authentication authentication);

}
