package dl.news.portal.domain.dto;

import com.fasterxml.jackson.annotation.JsonView;
import dl.news.portal.domain.entity.User;
import dl.news.portal.utils.ValidationMode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserDto {
    @Size(min = 3, max = 50)
    private String username;

    @Email(regexp = "[A-Za-z0-9._%-+]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}", groups = {ValidationMode.Create.class, ValidationMode.Update.class})
    @Size(max = 100, groups = {ValidationMode.Create.class, ValidationMode.Update.class})
    @NotBlank(groups = ValidationMode.Create.class)
    private String email;
    @Size(min = 6, max = 255)
    private String password;

    public UserDto(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public UserDto() {
    }

    public void transfer(User receiver) {
        if (username != null) {
            receiver.setUsername(username);
        }
        if (email != null) {
            receiver.setEmail(email);
        }
        if (password != null) {
            receiver.setPassword(password);
        }
    }

    public static User of(UserDto userDto) {
        User user = new User();
        userDto.transfer(user);
        return user;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonView(ValidationMode.Create.class)
    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
