package dl.news.portal.domain.dto;

import dl.news.portal.domain.entity.User;

import java.util.Optional;

public class UserCreatingDto implements DtoTransoform<User>, DtoTransfer<User> {
    private Optional<String> username;
    private Optional<String> email;
    private Optional<String> password;

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
