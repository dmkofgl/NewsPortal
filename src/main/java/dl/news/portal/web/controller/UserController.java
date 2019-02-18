package dl.news.portal.web.controller;

import dl.news.portal.domain.dto.UserCreatingDto;
import dl.news.portal.domain.dto.UserSearchingDto;
import dl.news.portal.domain.dto.UserUpdateDto;
import dl.news.portal.domain.entity.User;
import dl.news.portal.domain.resource.PageResource;
import dl.news.portal.domain.resource.UserResource;
import dl.news.portal.domain.service.UserService;
import dl.news.portal.exception.EntityNotExistsException;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", paramType = "query"),
            @ApiImplicitParam(name = "email", paramType = "query")
    })
    public PageResource<UserResource> getFilteredUsers(UserSearchingDto dto, Pageable pageable) {
        Page<UserResource> userPage = userService.getFilteredPage(dto, pageable).map(UserResource::new);
        return new PageResource<>(userPage);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> addUser(@Valid @RequestBody UserCreatingDto user) {
        userService.createUser(user.transform());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateDto updateUser) {
        userService.updateUser(id, updateUser);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
