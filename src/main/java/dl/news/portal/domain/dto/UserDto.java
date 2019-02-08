package dl.news.portal.domain.dto;

import dl.news.portal.domain.entity.User;

import java.util.Optional;

public class UserDto {
    private Optional<String> optionalUsername;
    private Optional<String> optionalEmail;

    public UserDto(String optionalUsername, String email) {
        this.optionalUsername = Optional.ofNullable(optionalUsername);
        this.optionalEmail = Optional.ofNullable(email);
    }

    public UserDto() {
        this(null, null);
    }

    public void mapToUser(User user) {
        optionalUsername.ifPresent(user::setUsername);
        optionalEmail.ifPresent(user::setEmail);
    }
}
