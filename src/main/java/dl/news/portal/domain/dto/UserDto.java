package dl.news.portal.domain.dto;

import dl.news.portal.domain.entity.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.Optional;

public class UserDto implements DtoTransfer<User> {

    private Optional<@Size(min = 3, max = 50) String> username;

    private Optional<
            @Email(regexp = "[A-Za-z0-9._%-+]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}")
            @Size(max = 100)
                    String> email;

    public void setUsername(String username) {
        this.username = Optional.ofNullable(username);
    }

    public void setEmail(String email) {
        this.email = Optional.ofNullable(email);
    }

    public UserDto(String username, String email) {
        this.username = Optional.ofNullable(username);
        this.email = Optional.ofNullable(email);
    }

    public UserDto() {
        this(null, null);
    }

    @Override
    public void transfer(User receiver) {
        username.ifPresent(receiver::setUsername);
        email.ifPresent(receiver::setEmail);
    }
}
