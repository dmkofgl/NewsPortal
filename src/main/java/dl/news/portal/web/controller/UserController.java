package dl.news.portal.web.controller;

import dl.news.portal.domain.dto.UserDto;
import dl.news.portal.domain.entity.User;
import dl.news.portal.domain.response.PageResponse;
import dl.news.portal.domain.response.UserResponse;
import dl.news.portal.domain.response.exception.BindExceptionResponse;
import dl.news.portal.domain.response.exception.ErrorResponse;
import dl.news.portal.domain.service.UserService;
import dl.news.portal.utils.ValidationMode;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.security.Principal;

@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    private static final String ID_PATH = "/{id}";
    private static final String ME = "/me";

    @Autowired
    private UserService userService;

    @GetMapping(ME)
    public UserResponse getCurrentUser(Principal principal) {
        return userService.findByUsername(principal.getName())
                .map(UserResponse::new)
                .orElseThrow(() -> new EntityNotFoundException("Unable to find user with username " + principal.getName()));
    }

    @GetMapping(ID_PATH)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Not Found", response = ErrorResponse.class)
    })
    public UserResponse getUserById(@PathVariable Long id) {
        return userService.findUserById(id)
                .map(UserResponse::new)
                .orElseThrow(() -> new EntityNotFoundException("Unable to find user with id " + id));
    }

    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(code = 422, message = "Validation error", response = BindExceptionResponse.class)
    })
    public PageResponse<UserResponse> getFilteredUsers(UserDto dto, Pageable pageable) {
        Page<UserResponse> userPage = userService.getFilteredPage(dto, pageable).map(UserResponse::new);
        return new PageResponse<>(userPage);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> addUser(@Validated(ValidationMode.Create.class) @RequestBody User userDto) {
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
