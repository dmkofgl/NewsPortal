package dl.news.portal.domain.service;

import dl.news.portal.domain.dto.UserDto;
import dl.news.portal.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();

    Page<User> getUsersPage(Pageable pageable);

    void createUser(UserDto user);

    void updateUser(Long id, UserDto updatedUser);

    void deleteUser(Long id);

    Optional<User> findUserById(Long id);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    List<User> findByUsername(String username, SearchingMode matcher);
}

