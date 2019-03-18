package dl.news.portal.web.config.security.jwt;

import dl.news.portal.domain.dto.UserDto;
import dl.news.portal.domain.entity.RefreshToken;
import dl.news.portal.domain.service.RefreshTokenService;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class TokenProvider {
    private static final String AUTHORITIES_KEY = "scopes";
    private static final String SIGNING_KEY = "newsPortal";
    private static final long ACCESS_TOKEN_VALIDITY_MILLISECONDS = 180_000L;

    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private AuthenticationManager authenticationManager;

    public String getSubFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SIGNING_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateAccessToken(UserDto userDto) {
        Authentication authentication = authenticateUser(userDto);
        final String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(SignatureAlgorithm.HS256, SIGNING_KEY)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY_MILLISECONDS))
                .compact();
    }

    private Authentication authenticateUser(UserDto userDto) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userDto.getUsername(),
                        userDto.getPassword()
                )
        );
    }

    public String generateRefreshToken(UserDto userDto) {
        Authentication authentication = authenticateUser(userDto);
        return refreshTokenService.takeRefreshToken(authentication);
    }

    public String refreshToken(RefreshToken refreshToken) {
        refreshToken.setActive(false);
        UserDto userDto = new UserDto(refreshToken.getOwner());
        refreshTokenService.saveRefreshToken(refreshToken);
        return generateRefreshToken(userDto);
    }

    public Boolean validateAccessToken(String token, UserDetails userDetails) {
        final String username = getSubFromToken(token);
        return (username.equals(userDetails.getUsername())
                && !isTokenExpired(token));
    }

    public RefreshToken validateRefreshToken(String token) {
        Long tokenId = Long.parseLong(getSubFromToken(token));
        RefreshToken refreshToken = refreshTokenService.getById(tokenId).orElseThrow(() -> new IllegalArgumentException("Token does not exist."));
        if (!refreshToken.getActive()) {
            throw new IllegalArgumentException("Token had been used.");
        }
        return refreshToken;
    }

    UsernamePasswordAuthenticationToken getAuthentication(final String token, final UserDetails userDetails) {
        final JwtParser jwtParser = Jwts.parser().setSigningKey(SIGNING_KEY);
        final Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);
        final Claims claims = claimsJws.getBody();
        final Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }
}
