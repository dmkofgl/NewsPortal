package dl.news.portal.domain.service;

import dl.news.portal.domain.dto.UserDto;
import dl.news.portal.domain.entity.User;
import dl.news.portal.exception.DeniedParameterException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserServiceTest {
    @Autowired
    private UserService userService;


    @Test
    public void findById() {
        Optional<User> user = userService.findUserById(1L);
        assertTrue(user.isPresent());
    }

    @Test
    public void addUser_whenUserDtoIsValid_shouldChangeSize() {
        Long size = userService.count();
        UserDto user = new UserDto("nAMe2Test", "mailcom@ccs.cc", "password");
        userService.createUser(user);
        assertNotEquals(size, userService.count());
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

    @Test
    public void findByUsernameSpecification() {
        final long COUNT_ALL = userService.count();
        UserDto usernameDto = new UserDto("est", null, null);
        PageRequest pageRequest = new PageRequest(0, 5);

        Page<User> userPageByUsername = userService.getFilteredPage(usernameDto, pageRequest);

        assertNotEquals(COUNT_ALL, userPageByUsername.getTotalElements());
        assertNotEquals(0L, userPageByUsername.getTotalElements());
    }

    @Test
    public void findByEmailSpecification() {
        final long COUNT_ALL = userService.count();
        UserDto emailDto = new UserDto(null, "com", null);
        PageRequest pageRequest = new PageRequest(0, 5);

        Page<User> userPageByEmail = userService.getFilteredPage(emailDto, pageRequest);

        assertNotEquals(COUNT_ALL, userPageByEmail.getTotalElements());
        assertNotEquals(0L, userPageByEmail.getTotalElements());
    }

    @Test
    public void findByUsernameAndEmailSpecification() {
        final long COUNT_ALL = userService.count();
        UserDto usernameAndEmailDto = new UserDto("est", "com", null);
        PageRequest pageRequest = new PageRequest(0, 5);

        Page<User> userPageByUsernameAndEmail = userService.getFilteredPage(usernameAndEmailDto, pageRequest);

        assertNotEquals(COUNT_ALL, userPageByUsernameAndEmail.getTotalElements());
        assertNotEquals(0L, userPageByUsernameAndEmail.getTotalElements());
    }
}