package dl.news.portal.domain.dto;

import dl.news.portal.domain.entity.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.Optional;

public class UserCreatingDto implements DtoTransform<User>, DtoTransfer<User> {
    private Optional<@Size(min = 3, max = 50) String> username;
    private Optional<
            @Email(regexp = "[A-Za-z0-9._%-+]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}")
            @Size(max = 100)
                    String> email;
    private Optional<@Size(min = 6, max = 255) String> password;

    @Override
    public void transfer(User receiver) {
        username.ifPresent(receiver::setUsername);
        email.ifPresent(receiver::setEmail);
        password.ifPresent(receiver::setPassword);
    }

    @Override
    public User transform() {
        User user = new User();
        transfer(user);
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
}
