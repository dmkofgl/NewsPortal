package dl.news.portal.domain.service;

import dl.news.portal.domain.dto.UserDto;
import dl.news.portal.domain.entity.OauthUser;
import dl.news.portal.domain.entity.User;
import dl.news.portal.domain.entity.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {
    Page<UserProfile> getFilteredPage(UserDto dto, Pageable pageable);

    Optional<UserProfile> findByUsername(String username);

    Optional<User> findUserByUsername(String username);

    void createUser(User user);

    void createOauthUser(OauthUser user);

    void updateUser(Long id, UserDto updatedUser);

    void deleteUser(Long id);

    Optional<UserProfile> findUserById(Long id);

    Long count();
}

