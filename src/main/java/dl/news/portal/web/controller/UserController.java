package dl.news.portal.web.controller;

import dl.news.portal.domain.dto.CreatingUserDto;
import dl.news.portal.domain.dto.UserDto;
import dl.news.portal.domain.entity.User;
import dl.news.portal.domain.resource.PageResource;
import dl.news.portal.domain.resource.UserResource;
import dl.news.portal.domain.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public UserResource getUserById(@PathVariable Long id) {
        return userService.findUserById(id)
                .map(UserResource::new)
                .orElseThrow(RuntimeException::new);
    }

    @GetMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve (0..N)"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "Number of records per page."),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "Sorting criteria in the format: property(,asc|desc). " +
                            "Default sort order is ascending. " +
                            "Multiple sort criteria are supported.")
    })
    public PageResource<User> getAllUsers(Pageable pageable) {
        Page<User> userPage = userService.getUsersPage(pageable);
        PageResource<User> resource = new PageResource<>(userPage);
        return resource;

    }

    @PostMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "email", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "password", dataType = "String", paramType = "query")
    })
    public ResponseEntity<HttpStatus> addUser(CreatingUserDto user) {
        userService.createUser(user.transform());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> updateUser(@PathVariable Long id, UserDto updateUser) {
        userService.updateUser(id, updateUser);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);

    }
}
