package dl.news.portal.domain.repository;

import dl.news.portal.domain.entity.User;
import dl.news.portal.web.config.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
@Transactional
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void findByEmail() {
        Optional<User> user = userRepository.findByEmail("email@gmail.com");
        assertTrue(user.isPresent());
    }

    @Test
    public void findById() {
        Optional<User> user = userRepository.findById(1L);
        assertTrue(user.isPresent());
    }

    @Test
    public void findAll() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        assertTrue(users.size() != 0);
    }

    @Test
    public void findByUsername() {
        Optional<User> user = userRepository.findByUsername("nameTest");
        assertTrue(user.isPresent());
    }

    @Test
    public void findByUsernameIgnoreCaseContaining() {
        List<User> users = userRepository.findByUsernameIgnoreCaseContaining("NaMe");
        assertTrue(users.size() != 0);
    }

    @Test
    public void add() {
        User user = new User();
        user.setPassword("pws123");
        user.setUsername("user");
        user.setEmail("mailcom@ccs.cc");

        userRepository.save(user);

        assertNotNull(user.getId());
    }

    @Test(expected = ConstraintViolationException.class)
    public void addUserWithPasswordException() {
        User user = new User();
        user.setPassword("pws12");
        user.setUsername("user");
        user.setEmail("mailcom@ccs.cc");
        userRepository.save(user);
    }
}