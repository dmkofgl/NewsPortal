package dl.news.portal.domain.dto;

import dl.news.portal.domain.entity.User;

import java.util.Optional;

public class CreatingUserDto implements DtoTransofrm<User>, DtoTransfer<User> {
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

    public void setUsername(Optional<String> username) {
        this.username = username;
    }

    public void setEmail(Optional<String> email) {
        this.email = email;
    }

    public void setPassword(Optional<String> password) {
        this.password = password;
    }
}
