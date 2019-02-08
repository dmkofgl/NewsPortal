package dl.news.portal.domain.dto;

import dl.news.portal.domain.entity.User;

import java.util.Optional;

public class UserDto implements DtoTransfer<User> {
    private Optional<String> optionalUsername;
    private Optional<String> optionalEmail;

    public UserDto(String username, String email) {
        this.optionalUsername = Optional.ofNullable(username);
        this.optionalEmail = Optional.ofNullable(email);
    }

    public UserDto() {
        this(null, null);
    }

    @Override
    public void transfer(User receiver) {
        optionalUsername.ifPresent(receiver::setUsername);
        optionalEmail.ifPresent(receiver::setEmail);
    }
}
