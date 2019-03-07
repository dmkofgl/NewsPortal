package dl.news.portal.domain.service.impl;

import dl.news.portal.domain.entity.RefreshToken;
import dl.news.portal.domain.entity.User;
import dl.news.portal.domain.repository.RefreshTokenRepository;
import dl.news.portal.domain.service.RefreshTokenService;
import dl.news.portal.domain.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private static final long REFRESH_TOKEN_VALIDITY_SECONDS = 5 * 60 * 60 * 24;
    private static final String SIGNING_KEY = "newsPortal";
    private static final String AUTHORITIES_KEY = "scopes";

    @Autowired
    private UserService userService;
    @Autowired
    private RefreshTokenRepository tokenRepository;

    @Override
    public RefreshToken createRefreshToken(Authentication authentication) {
        final String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        RefreshToken refreshToken = new RefreshToken();
        User owner = userService.findUserByUsername(authentication.getName()).orElseThrow(RuntimeException::new);
        refreshToken.setOwner(owner);
        refreshToken.setIssuedAt(new Date());
        refreshToken.setExpiration(new Date(refreshToken.getIssuedAt().getTime() + REFRESH_TOKEN_VALIDITY_SECONDS));
        refreshToken.setAuthorities(authorities);
        refreshToken.setActive(true);
        return refreshToken;
    }


    @Override
    public Optional<RefreshToken> getById(Long id) {
        return tokenRepository.findById(id);
    }

    @Override
    public void saveRefreshToken(RefreshToken token) {
        tokenRepository.save(token);
    }

    @Override
    public List<RefreshToken> getTokens(Authentication authentication) {
        User owner = userService.findUserByUsername(authentication.getName()).orElseThrow(RuntimeException::new);
        return tokenRepository.findAllByOwner(owner);
    }

    @Override
    public String buildToken(RefreshToken refreshToken) {
        return Jwts.builder()
                .setSubject(refreshToken.getId().toString())
                .claim(AUTHORITIES_KEY, refreshToken.getAuthorities())
                .signWith(SignatureAlgorithm.HS256, SIGNING_KEY)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY_SECONDS * 1000))
                .compact();
    }

    public String takeRefreshToken(Authentication authentication) {
        RefreshToken refreshToken = createRefreshToken(authentication);
        saveRefreshToken(refreshToken);
        return buildToken(refreshToken);
    }

    @Override
    public Boolean checkToken(RefreshToken refreshToken) {
        return tokenRepository.existsById(refreshToken.getId());
    }
}
