package dl.news.portal.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RefreshTokenDto {
    @JsonProperty("refresh_token")
    String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}
