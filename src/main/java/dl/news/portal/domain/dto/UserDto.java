package dl.news.portal.domain.dto;

import dl.news.portal.domain.entity.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.Optional;

public class UserDto {
    private Optional<@Size(min = 3, max = 50) String> username;
    private Optional<
            @Email(regexp = "[A-Za-z0-9._%-+]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}")
            @Size(max = 100)
                    String> email;

    private Optional<@Size(min = 6, max = 255) String> password;

    public UserDto(String username, String email, String password) {
        this.username = Optional.ofNullable(username);
        this.email = Optional.ofNullable(email);
        this.password = Optional.ofNullable(password);
    }

    public UserDto() {
        this(null, null, null);
    }

    public void transfer(User receiver) {
        username.ifPresent(receiver::setUsername);
        email.ifPresent(receiver::setEmail);
        password.ifPresent(receiver::setPassword);
    }

    public static User of(UserDto userDto) {
        User user = new User();
        userDto.transfer(user);
        return user;
    }

    public void setUsername(String username) {
        this.username = Optional.ofNullable(username);
    }

    public void setEmail(String email) {
        this.email = Optional.ofNullable(email);
    }

    public void setPassword(String password) {
        this.password = Optional.ofNullable(password);
    }

    public Optional<String> getPassword() {
        return password;
    }
}
