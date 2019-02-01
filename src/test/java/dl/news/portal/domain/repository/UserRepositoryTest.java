package dl.news.portal.domain.repository;

import dl.news.portal.domain.entity.User;
import dl.news.portal.web.config.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
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
        int sizeBefore, sizeAfter;
        User user = new User();
        user.setId(222L);
        user.setPassword("pws123");
        user.setUsername("user");
        user.setEmail("mailcom@ccs.cc");
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        sizeBefore = users.size();

        userRepository.save(user);

        users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        sizeAfter = users.size();
        assertEquals(1, sizeAfter - sizeBefore);
    }
}