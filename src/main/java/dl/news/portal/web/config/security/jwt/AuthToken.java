package dl.news.portal.web.config.security.jwt;

public class AuthToken {
    private String accessToken;

    public AuthToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
