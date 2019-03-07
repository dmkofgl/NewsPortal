package dl.news.portal.web.config.security.jwt;

import dl.news.portal.domain.dto.RefreshTokenDto;
import dl.news.portal.domain.dto.UserDto;
import dl.news.portal.domain.entity.AuthToken;
import dl.news.portal.domain.entity.RefreshToken;
import dl.news.portal.domain.response.exception.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/token")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenProvider jwtTokenUtil;

    @RequestMapping(value = "/generate", method = RequestMethod.POST)
    public ResponseEntity getAuthToken(@RequestBody UserDto loginUser) throws AuthenticationException {

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getUsername(),
                        loginUser.getPassword()
                )
        );
        final String accessToken = jwtTokenUtil.generateAccessToken(authentication);
        final String refreshToken = jwtTokenUtil.generateRefreshToken(authentication);
        return ResponseEntity.ok(new AuthToken(accessToken, refreshToken));
    }

    @RequestMapping(value = "/refresh", method = RequestMethod.POST)
    public ResponseEntity getAuthToken(@RequestBody RefreshTokenDto oldRefreshToken) throws AuthenticationException {
        RefreshToken refreshToken = jwtTokenUtil.validateRefreshToken(oldRefreshToken.getRefreshToken());

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        refreshToken.getOwner().getUsername(),
                        refreshToken.getOwner().getPassword()
                )
        );
        String newRefreshToken = jwtTokenUtil.refreshToken(authentication, refreshToken);
        String newAccessToken = jwtTokenUtil.generateAccessToken(authentication);

        return ResponseEntity.ok(new AuthToken(newAccessToken, newRefreshToken));
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorResponse resources = new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.getReasonPhrase());
        return new ResponseEntity<>(resources, HttpStatus.BAD_REQUEST);
    }
}
