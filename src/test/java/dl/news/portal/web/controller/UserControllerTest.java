package dl.news.portal.web.controller;

import dl.news.portal.domain.entity.User;
import dl.news.portal.domain.resource.PageResource;
import dl.news.portal.domain.resource.UserResource;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void getUserById() {
        ResponseEntity template = restTemplate.getForEntity("/users/1", UserResource.class);
        UserResource body = ((UserResource) template.getBody());
        Assert.assertEquals(template.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(body.getUser().getUsername(), "name");
        Assert.assertEquals(body.getLink("self"), "/users/1");
    }

    @Test
    public void getAllUsers() {
        ResponseEntity b = restTemplate.getForEntity("/users", PageResource.class);
        PageResource<User> body = (PageResource<User>) b.getBody();
        Assert.assertEquals(b.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void addUser() {
    }

    @Test
    public void updateUser() {
    }

    @Test
    public void deleteUser() {
    }
}