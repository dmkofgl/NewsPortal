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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.refEq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

        mockMvc.perform(get(path))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$._links.self").hasJsonPath());
    }

    @Test
    public void getUsers_whenDtoIsEmpty_shouldReturnOk() throws Exception {
        List<User> userList = new ArrayList<>();
        User user = new User();
        user.setId(1L);
        user.setUsername("testUsername");
        user.setEmail("testEmail@mail.com");
        userList.add(user);
        Page<User> userPage = new PageImpl<>(userList);
        Mockito.when(userService.getFilteredPage(any(UserDto.class), any(Pageable.class))).thenReturn(userPage);

        mockMvc.perform(get(USERS_PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.userResponseList[0].username").value(user.getUsername()))
                .andExpect(jsonPath("$._embedded.userResponseList[0].email").value(user.getEmail()))
                .andExpect(jsonPath("$.page").hasJsonPath())
                .andExpect(jsonPath("$._links.self").hasJsonPath());
    }

    @Test
    public void getUsers_whenModifyPage_shouldReturnOk() throws Exception {
        List<User> userList = new ArrayList<>();
        Pageable pageable = new PageRequest(0, 1);
        User user = new User();
        user.setId(1L);
        user.setUsername("testUsername");
        user.setEmail("testEmail@mail.com");
        userList.add(user);
        Page<User> userPage = new PageImpl<>(userList, pageable, 2L);

        Mockito.when(userService.getFilteredPage(any(UserDto.class), any(Pageable.class))).thenReturn(userPage);

        mockMvc.perform(get(USERS_PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").hasJsonPath())
                .andExpect(jsonPath("$.page.size").value(1))
                .andExpect(jsonPath("$.page.number").value(0))
                .andExpect(jsonPath("$._links.self").hasJsonPath());
    }

    @Test
    public void getUsers_whenExistFilteringByEmail_shouldReturnOk() throws Exception {
        final int FILTERED_COUNT = 1;
        List<User> userList = new ArrayList<>();
        User user = new User();
        user.setId(1L);
        user.setUsername("testUsername");
        user.setEmail("testEmail@mail.com");
        userList.add(user);
        UserDto dto = new UserDto();
        dto.setEmail("temail");
        Page<User> userPage = new PageImpl<>(userList);
        Mockito.when(userService.getFilteredPage(refEq(dto), any(Pageable.class))).thenReturn(userPage);

        mockMvc.perform(get(USERS_PATH).param("email", dto.getEmail()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").hasJsonPath())
                .andExpect(jsonPath("$._embedded.userResponseList[0].username").value(user.getUsername()))
                .andExpect(jsonPath("$._embedded.userResponseList[0].email").value(user.getEmail()))
                .andExpect(jsonPath("$._embedded.userResponseList[1]").doesNotExist())
                .andExpect(jsonPath("$.page.totalElements").value(FILTERED_COUNT));
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

        mockMvc.perform(post(USERS_PATH)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void addUser_whenUserDtoIsNotValid_shouldReturnUnprocessableEntity() throws Exception {
        UserDto dto = new UserDto("testUsername", "qwerty", "testPassword");
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String body = mapper.writeValueAsString(dto);

        mockMvc.perform(post(USERS_PATH)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors[0].field").value("email"));
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
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors[0].field").value("email"));
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
        UserDto dto = new UserDto("testUsername", "mail@tester.cd", "asddfdsfdsf");
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String body = mapper.writeValueAsString(dto);

        mockMvc.perform(patch(path)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors[0].field").value("password"));
    }


}