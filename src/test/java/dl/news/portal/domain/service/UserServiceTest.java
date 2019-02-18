package dl.news.portal.domain.service;

import dl.news.portal.domain.dto.UserUpdateDto;
import dl.news.portal.domain.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
        assertEquals(users.size(), userService.count().intValue());
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
    public void updateUser() {
        String newUsername = "testTester", newEmail = "sven@qwert.ce";
        UserUpdateDto updatedUser = new UserUpdateDto(newUsername, newEmail);
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

    @Test
    public void findBySpecification() {
        final long COUNT_ALL = userService.count();
        UserSearchingDto usernameDto = new UserSearchingDto("est", null);
        UserSearchingDto emailDto = new UserSearchingDto(null, "com");
        UserSearchingDto usernameAndEmailDto = new UserSearchingDto("est", "com");
        PageRequest pageRequest = new PageRequest(0, 5);

        Page<User> userPageByEmail = userService.getFilteredPage(emailDto, pageRequest);
        Page<User> userPageByUsername = userService.getFilteredPage(usernameDto, pageRequest);
        Page<User> userPageByUsernameAndEmail = userService.getFilteredPage(usernameAndEmailDto, pageRequest);

        assertNotEquals(COUNT_ALL, userPageByEmail.getTotalElements());
        assertNotEquals(0L, userPageByEmail.getTotalElements());
        assertNotEquals(COUNT_ALL, userPageByUsername.getTotalElements());
        assertNotEquals(0L, userPageByUsername.getTotalElements());
        assertNotEquals(COUNT_ALL, userPageByUsernameAndEmail.getTotalElements());
        assertNotEquals(0L, userPageByUsernameAndEmail.getTotalElements());
    }

}