package dl.news.portal.domain.service;

import dl.news.portal.domain.dto.UserDto;
import dl.news.portal.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {
    Page<User> getFilteredPage(UserDto dto, Pageable pageable);

    Optional<User> findByUsername(String username);

    void createUser(UserDto user);


    void updateUser(Long id, UserDto updatedUser);

    void deleteUser(Long id);

    Optional<User> findUserById(Long id);

    Long count();
}

