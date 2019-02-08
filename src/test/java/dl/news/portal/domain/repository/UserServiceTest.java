package dl.news.portal.domain.repository;

import dl.news.portal.domain.dto.UserDto;
import dl.news.portal.domain.entity.User;
import dl.news.portal.domain.service.SearchingMode;
import dl.news.portal.domain.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    public void findByEmail() {
        Optional<User> user = userService.findByEmail("email@gmail.com");
        assertTrue(user.isPresent());
    }

    @Test
    public void findById() {
        Optional<User> user = userService.findUserById(1L);
        assertTrue(user.isPresent());
    }

    @Test
    public void findAll() {
        List<User> users = userService.getAllUsers();
        assertTrue(users.size() != 0);
    }

    @Test
    public void findByUsername() {
        Optional<User> user = userService.findByUsername("nameTest");
        assertTrue(user.isPresent());
    }

    @Test
    public void findByUsernameIgnoreCase() {
        List<User> users = userService.findByUsername("NaMe", SearchingMode.IGNORE_CASE);
        assertTrue(users.size() != 0);
    }

    @Test
    public void findByUsernameIdenticalCase() {
        List<User> users = userService.findByUsername("name", SearchingMode.IDENTICAL);
        assertTrue(users.size() != 0);
    }

    @Test
    public void findByUsernameIdenticalCaseNegative() {
        List<User> users = userService.findByUsername("NAMe", SearchingMode.IDENTICAL);
        assertEquals(0, users.size());
    }

    @Test
    public void add() {
        User user = new User();
        user.setPassword("pws123");
        user.setUsername("user");
        user.setEmail("mailcom@ccs.cc");

        userService.createUser(user);

        assertNotNull(user.getId());
    }

    @Test
    public void updateUser() {
        User userBeforeUpdate = userService.findUserById(1L).get();
        UserDto updatedUser = new UserDto("updatedUser", "updatedEmail");
        userService.updateUser(1L, updatedUser);
        User userAfterUpdate = userService.findUserById(1L).get();
        assertEquals("updatedUser", userAfterUpdate.getUsername());
    }

    @Test(expected = ConstraintViolationException.class)
    public void addUserWithPasswordException() {
        User user = new User();
        user.setPassword("pws12");
        user.setUsername("user");
        user.setEmail("mailcom@ccs.cc");
        userService.createUser(user);
    }
}