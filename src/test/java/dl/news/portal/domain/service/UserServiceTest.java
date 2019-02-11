package dl.news.portal.domain.service;

import dl.news.portal.domain.dto.UserDto;
import dl.news.portal.domain.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
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
        assertEquals(users.size(), 2);
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
    public void add() {
        User user = new User();
        user.setPassword("password");
        user.setUsername("nAMe2Test");
        user.setEmail("mailcom@ccs.cc");

        userService.createUser(user);

        assertNotNull(user.getId());
    }

    @Test
    @Rollback
    public void updateUser() {
        String newUsername = "testTester", newEmail = "sven@qwert.ce";
        UserDto updatedUser = new UserDto(newUsername, newEmail);
        userService.updateUser(2L, updatedUser);
        User userAfterUpdate = userService.findUserById(2L).get();
        assertEquals(newUsername, userAfterUpdate.getUsername());
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