package dl.news.portal.domain.service.impl;

import dl.news.portal.domain.dto.UserDto;
import dl.news.portal.domain.entity.OauthUser;
import dl.news.portal.domain.entity.User;
import dl.news.portal.domain.entity.UserProfile;
import dl.news.portal.domain.repository.OauthUserRepository;
import dl.news.portal.domain.repository.UserProfileRepository;
import dl.news.portal.domain.repository.UserRepository;
import dl.news.portal.domain.service.UserService;
import dl.news.portal.exception.DeniedParameterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.Predicate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserProfileRepository userProfileRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OauthUserRepository oauthUserRepository;

    @Override
    public Optional<UserProfile> findUserById(Long id) {
        return userProfileRepository.findById(id);
    }

    @Override
    public void createUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void createOauthUser(OauthUser user) {
        oauthUserRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userProfileRepository.deleteById(id);
    }

    @Override
    public void updateUser(Long id, UserDto updatedUser) {
        if (updatedUser.getPassword() != null) {
            throw new DeniedParameterException();
        }
        User user = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        updatedUser.transfer(user);
        userRepository.save(user);
    }

    @Override
    public Page<UserProfile> getFilteredPage(UserDto dto, Pageable pageable) {
        Specification<UserProfile> specification = getSpecification(dto);
        return userProfileRepository.findAll(specification, pageable);
    }

    @Override
    public Optional<UserProfile> findByUsername(String username) {
        return userProfileRepository.findByUsername(username);
    }

    @Override
    public Long count() {
        return userProfileRepository.count();
    }

    private void transformDto(User receiver, UserDto dto) {
        String email = dto.getEmail();
        String username = dto.getUsername();
        String password = dto.getPassword();

        if (email != null) {
            receiver.setEmail(email);
        }

        if (username != null) {
            receiver.setUsername(username);
        }

        if (password != null) {
            receiver.setPassword(password);
        }
    }

    private Specification<UserProfile> getSpecification(UserDto userDto) {
        String username = userDto.getUsername();
        return (root, criteriaQuery, criteriaBuilder) -> {
            Collection<Predicate> predicates = new HashSet<>();
            if (username != null) {
                predicates.add(criteriaBuilder.like(root.get("username"), "%" + username + "%"));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
