package dl.news.portal.web.controller;

import dl.news.portal.domain.dto.RefreshTokenDto;
import dl.news.portal.domain.dto.UserDto;
import dl.news.portal.domain.entity.AuthToken;
import dl.news.portal.domain.entity.RefreshToken;
import dl.news.portal.web.config.security.jwt.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/token")
public class JwtAuthenticationController {

    @Autowired
    private TokenProvider jwtTokenUtil;

    @RequestMapping(value = "/generate", method = RequestMethod.POST)
    public ResponseEntity getAuthToken(@RequestBody UserDto loginUser) throws AuthenticationException {


        final String accessToken = jwtTokenUtil.generateAccessToken(loginUser);
        final String refreshToken = jwtTokenUtil.generateRefreshToken(loginUser);
        return ResponseEntity.ok(new AuthToken(accessToken, refreshToken));
    }

    @RequestMapping(value = "/refresh", method = RequestMethod.POST)
    public ResponseEntity getAuthToken(@RequestBody RefreshTokenDto oldRefreshToken) throws AuthenticationException {
        RefreshToken refreshToken = jwtTokenUtil.validateRefreshToken(oldRefreshToken.getRefreshToken());
        UserDto userDto = new UserDto(refreshToken.getOwner());
        String newRefreshToken = jwtTokenUtil.refreshToken(refreshToken);
        String newAccessToken = jwtTokenUtil.generateAccessToken(userDto);

        return ResponseEntity.ok(new AuthToken(newAccessToken, newRefreshToken));
    }
}
