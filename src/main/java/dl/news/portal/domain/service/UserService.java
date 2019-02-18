package dl.news.portal.domain.service;

import dl.news.portal.domain.dto.DtoTransfer;
import dl.news.portal.domain.dto.SearchingSpecification;
import dl.news.portal.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {
    Page<User> getFilteredPage(SearchingSpecification<User> dto, Pageable pageable);

    void createUser(User user);

    void updateUser(Long id, DtoTransfer<User> updatedUser);

    void deleteUser(Long id);

    Optional<User> findUserById(Long id);

    Long count();
}

