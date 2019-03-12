package dl.news.portal.web.config.oauth2;

import dl.news.portal.domain.dto.UserDto;
import dl.news.portal.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class OauthAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        super.onAuthenticationSuccess(httpServletRequest, httpServletResponse, authentication);
        OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) authentication;
        httpServletResponse.addHeader("token", ((OAuth2AuthenticationDetails) oAuth2Authentication.getDetails()).getTokenValue());
        if (!userService.findByUsername(oAuth2Authentication.getName()).isPresent()) {
            userService.createUser(createNewUserDto(oAuth2Authentication.getUserAuthentication()));
        }
    }

    private UserDto createNewUserDto(Authentication authentication) {
        String email = (String) ((Map) authentication.getDetails()).get("email");
        UserDto userDto = new UserDto();
        userDto.setUsername(authentication.getName());
        userDto.setPassword("password");
        userDto.setEmail(email);
        return userDto;
    }

}
