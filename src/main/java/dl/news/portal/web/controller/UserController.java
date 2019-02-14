package dl.news.portal.web.controller;

import dl.news.portal.domain.dto.UserCreatingDto;
import dl.news.portal.domain.dto.UserDto;
import dl.news.portal.domain.entity.User;
import dl.news.portal.domain.resource.PageResource;
import dl.news.portal.domain.resource.UserResource;
import dl.news.portal.domain.service.UserService;
import dl.news.portal.exception.EntityNotExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public UserResource getUserById(@PathVariable Long id) {
        return userService.findUserById(id)
                .map(UserResource::new)
                .orElseThrow(() -> new EntityNotExistsException(User.class.getSimpleName(), "Unable to find with id " + id));
    }

    @GetMapping
    public PageResource<UserResource> getAllUsers(Pageable pageable) {
        Page<UserResource> userPage = userService.getUsersPage(pageable).map(UserResource::new);
        return new PageResource<>(userPage);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> addUser(@Valid @RequestBody UserCreatingDto user) {
        userService.createUser(user.transform());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> updateUser(@PathVariable Long id, @Valid @RequestBody UserDto updateUser) {
        userService.updateUser(id, updateUser);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
