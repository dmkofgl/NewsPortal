package dl.news.portal.web.controller;

import dl.news.portal.domain.entity.User;
import dl.news.portal.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{id}")
    public Resource<User> getUserById(@PathVariable Long id) {

        return userRepository.findById(id)
                .map(employee -> new Resource<>(employee))
                .orElse(new Resource<>(new User()));

    }

    @GetMapping
    public Resource<Page<User>> getAllUsers(Pageable pageable) {
        Page<User> userPage = userRepository.findAll(pageable);
        Resource<Page<User>> resource = new Resource<>(userPage);
        ControllerLinkBuilder linkTo = ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(UserController.class).getAllUsers(pageable));
        resource.add(linkTo.withRel("self"));
        return resource;

    }

    @PostMapping
    public ResponseEntity<HttpStatus> addUser(User user) {
        HttpStatus httpStatus = HttpStatus.OK;
        try {
            userRepository.save(user);
        } catch (Exception e) {
            httpStatus = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(httpStatus);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> updateUser(@PathVariable Long id, User updateUser) {
        HttpStatus httpStatus = HttpStatus.OK;
        try {
            userRepository.save(updateUser);
        } catch (Exception e) {
            httpStatus = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(httpStatus);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable Long id) {
        HttpStatus httpStatus = HttpStatus.OK;
        try {
            userRepository.deleteById(id);
        } catch (Exception e) {
            httpStatus = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(httpStatus);

    }
}
