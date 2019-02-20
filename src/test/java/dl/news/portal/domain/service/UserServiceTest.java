package dl.news.portal.domain.service;

import dl.news.portal.domain.dto.UserDto;
import dl.news.portal.domain.entity.User;
import dl.news.portal.exception.DeniedParameterException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
    public void addUser_whenUserDtoIsValid_shouldChangeSize() {
        int size = userService.getAllUsers().size();
        UserDto user = new UserDto("nAMe2Test", "mailcom@ccs.cc", "password");
        userService.createUser(user);
        assertNotEquals(size, userService.getAllUsers().size());
    }

    @Test
    public void updateUser_whenUserDtoIsValid_shouldUsernameEquals() {
        String newUsername = "testTester";
        String newEmail = "sven@qwert.ce";
        UserDto updatedUser = new UserDto(newUsername, newEmail, null);
        userService.updateUser(2L, updatedUser);
        User userAfterUpdate = userService.findUserById(2L).get();
        assertEquals(newUsername, userAfterUpdate.getUsername());
    }

    @Test(expected = DeniedParameterException.class)
    public void updateUser_whenPasswordExists_shouldReturnException() {
        String newUsername = "testTester", newEmail = "sven@qwert.ce";
        UserDto updatedUser = new UserDto(newUsername, newEmail, "password");
        userService.updateUser(2L, updatedUser);
    }

    @Test(expected = ConstraintViolationException.class)
    public void addUser_whenPasswordIsInvalid_shouldReturnException() {
        UserDto user = new UserDto("user", "mailcom@ccs.cc", "pws12");
        userService.createUser(user);
    }
}