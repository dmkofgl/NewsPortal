package dl.news.portal.web.config.security.oauth2;

import dl.news.portal.domain.entity.OauthUser;
import dl.news.portal.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
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
        String username = oAuth2Authentication.getName();
        if (!userService.findByUsername(username).isPresent()) {
            OauthUser oauthUser = createNewOauthUser(oAuth2Authentication.getUserAuthentication());
            userService.createOauthUser(oauthUser);
        }
    }

    private OauthUser createNewOauthUser(Authentication authentication) {
        Long id = new Long(((Map) authentication.getDetails()).get("id").toString());
        OauthUser oauthUser = new OauthUser();
        oauthUser.setUsername(authentication.getName());
        oauthUser.setIdFromProvider(id);

        return oauthUser;
    }

}
