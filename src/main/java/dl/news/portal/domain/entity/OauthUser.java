package dl.news.portal.domain.entity;

import javax.persistence.*;

@Entity
public class OauthUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @MapsId
    @OneToOne(targetEntity = UserProfile.class)
    @JoinColumn
    private UserProfile userProfile;
    @Enumerated
    private AuthProvider authProvider;
    private Long idFromProvider;

    {
        authProvider = AuthProvider.GITHUB;
        userProfile = new UserProfile();
    }

    public String getUsername() {
        return userProfile.getUsername();
    }

    public void setUsername(String username) {
        userProfile.setUsername(username);
    }

    public Long getId() {
        return userProfile.getId();
    }

    public void setId(Long id) {
        userProfile.setId(id);
    }

    public AuthProvider getProvider() {
        return authProvider;
    }

    public void setProvider(AuthProvider provider) {
        this.authProvider = provider;
    }

    public Long getIdFromProvider() {
        return idFromProvider;
    }

    public void setIdFromProvider(Long idFromProvider) {
        this.idFromProvider = idFromProvider;
    }

    public enum AuthProvider {
        GITHUB
    }
}
