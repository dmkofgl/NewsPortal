package dl.news.portal.web.controller;

import dl.news.portal.domain.dto.UserDto;
import dl.news.portal.domain.dto.UserSearchingDto;
import dl.news.portal.domain.response.PageResponse;
import dl.news.portal.domain.response.UserResponse;
import dl.news.portal.domain.service.UserService;
import dl.news.portal.utils.ValidationMode;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final String ID_PATH = "/{id}";

    @Autowired
    private UserService userService;

    @GetMapping(ID_PATH)
    public UserResponse getUserById(@PathVariable Long id) {
        return userService.findUserById(id)
                .map(UserResponse::new)
                .orElseThrow(() -> new EntityNotFoundException("Unable to find user with id " + id));
    }

    @GetMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", paramType = "query"),
            @ApiImplicitParam(name = "email", paramType = "query")
    })
    public PageResponse<UserResponse> getFilteredUsers(UserSearchingDto dto, Pageable pageable) {
        Page<UserResponse> userPage = userService.getFilteredPage(dto, pageable).map(UserResponse::new);
        return new PageResponse<>(userPage);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> addUser(@Validated(ValidationMode.Create.class) @RequestBody UserDto userDto) {
        userService.createUser(userDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping(ID_PATH)
    public ResponseEntity<HttpStatus> updateUser(@PathVariable Long id, @Validated(ValidationMode.Update.class) @RequestBody UserDto userDto) {
        userService.updateUser(id, userDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(ID_PATH)
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
