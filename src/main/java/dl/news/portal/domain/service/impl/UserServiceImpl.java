package dl.news.portal.domain.service.impl;

import dl.news.portal.domain.dto.UserDto;
import dl.news.portal.domain.entity.User;
import dl.news.portal.domain.repository.UserRepository;
import dl.news.portal.domain.service.SearchingMode;
import dl.news.portal.domain.service.UserService;
import dl.news.portal.exception.DeniedParameterException;
import dl.news.portal.exception.UnexpectedSearchingModeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Page<User> getUsersPage(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void createUser(UserDto dto) {
        User user = new User();
        transferDto(user, dto);
        userRepository.save(user);
    }


    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void updateUser(Long id, UserDto updatedUser) {
        if (updatedUser.getPassword() != null) {
            throw new DeniedParameterException();
        }
        User user = userRepository.getOne(id);
        updatedUser.transfer(user);
        userRepository.saveAndFlush(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> findByUsername(String username, SearchingMode mode) {
        List<User> result;
        switch (mode) {
            case IDENTICAL: {
                result = userRepository.findByUsernameContaining(username);
            }
            break;
            case IGNORE_CASE: {
                result = userRepository.findByUsernameIgnoreCaseContaining(username);
            }
            break;
            default: {
                throw new UnexpectedSearchingModeException("Searching mode " + mode.toString() + "doesn't supported");
            }
        }
        return result;
    }

    private void transferDto(User receiver, UserDto dto) {
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
}
