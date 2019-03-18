package dl.news.portal.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @MapsId
    @OneToOne
    @JoinColumn
    private UserProfile userProfile;

    @Size(min = 6, max = 255)
    @JsonIgnore
    private String password;
    @Email(regexp = "[A-Za-z0-9._%-+]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}")
    @Column(unique = true, length = 100)
    @Size(max = 100)
    private String email;

    {
        userProfile = new UserProfile();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return userProfile.getId();
    }

    public void setId(Long id) {
        userProfile.setId(id);
    }

    public String getUsername() {
        return userProfile.getUsername();
    }

    public void setUsername(String username) {
        userProfile.setUsername(username);
    }

}
