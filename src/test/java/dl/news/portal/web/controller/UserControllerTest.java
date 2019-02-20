package dl.news.portal.web.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import dl.news.portal.domain.dto.UserDto;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {
    private static final String USERS_PATH = "/users";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    @Test
    public void getUserById_whenUserExists_shouldReturnOk() throws Exception {
        final String path = USERS_PATH + "/1";
        User user = new User();
        user.setId(1L);
        user.setUsername("testUsername");
        user.setEmail("testEmail@mail.com");

        Mockito.when(userService.findUserById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(get(path)).andExpect(status().isOk());
    }

    @Test
    public void getUserById_whenUserDoesNotExist_shouldReturnNotFound() throws Exception {
        final String path = USERS_PATH + "/2";
        User user = new User();
        user.setId(1L);
        user.setUsername("testUsername");
        user.setEmail("testEmail@mail.com");

        Mockito.when(userService.findUserById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(get(path)).andExpect(status().isNotFound());
    }


    @Test
    public void addUser_whenUserDtoIsValid_shouldReturnCreated() throws Exception {
        UserDto dto = new UserDto("testUsername", "testEmail@mail.com", "testPassword");
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String body = mapper.writeValueAsString(dto);

        mockMvc.perform(post(USERS_PATH).content(body).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
    }

    @Test
    public void addUser_whenUserDtoIsNotValid_shouldReturnUnprocessableEntity() throws Exception {
        UserDto dto = new UserDto("testUsername", "qwerty", "testPassword");
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String body = mapper.writeValueAsString(dto);

        mockMvc.perform(post(USERS_PATH).content(body).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void addUser_whenUserDtoWithNullField_shouldReturnUnprocessableEntity() throws Exception {
        UserDto dto = new UserDto("testUsername", null, "testPassword");
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String body = mapper.writeValueAsString(dto);

        mockMvc.perform(post(USERS_PATH)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void updateUser_whenUserDtoWithNullField_shouldReturnOk() throws Exception {
        final String path = USERS_PATH + "/1";
        UserDto dto = new UserDto("testUsername", null, null);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String body = mapper.writeValueAsString(dto);

        mockMvc.perform(patch(path)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void updateUser_whenPasswordExist_shouldReturnUnprocessableEntity() throws Exception {
        final String path = USERS_PATH + "/1";
        UserDto dto = new UserDto("testUsername", null, "asddfdsfdsf");
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String body = mapper.writeValueAsString(dto);

        mockMvc.perform(patch(path)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }


}