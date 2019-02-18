package dl.news.portal.web.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import dl.news.portal.domain.dto.UserCreatingDto;
import dl.news.portal.domain.entity.User;
import dl.news.portal.domain.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    @Test
    public void getUserById() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("testUsername");
        user.setEmail("testEmail@mail.com");

        Mockito.when(userService.findUserById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/users/1")).andExpect(status().isOk());
        mockMvc.perform(get("/users/2")).andExpect(status().isNotFound());
    }

    @Test
    public void addUser() throws Exception {
        UserCreatingDto dto = new UserCreatingDto("testUsername", "testEmail@mail.com", "testPassword");
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String body = mapper.writeValueAsString(dto.transform());

        mockMvc.perform(post("/users").content(body).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
    }

    @Test
    public void addUserNegative() throws Exception {
        UserCreatingDto dto = new UserCreatingDto("testUsername", "qwerty", "testPassword");
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String body = mapper.writeValueAsString(dto.transform());

        mockMvc.perform(post("/users").content(body).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isUnprocessableEntity());
    }


}