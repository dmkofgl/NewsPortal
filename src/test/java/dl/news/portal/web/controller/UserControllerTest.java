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
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
public class UserControllerTest {
    private static final String USERS_PATH = "/users";
    private static final String USER_ID_PATH_TEMPLATE = USERS_PATH + "/{id}";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    @Test
    public void getUserById_whenUserExists_shouldReturnOk() throws Exception {
        final long ID = 1L;
        User user = new User();
        user.setId(ID);
        user.setUsername("testUsername");
        user.setEmail("testEmail@mail.com");

        Mockito.when(userService.findUserById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(get(USER_ID_PATH_TEMPLATE, ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$._links.self").hasJsonPath())
                .andDo(document("get-user/ok",
                        pathParameters(parameterWithName("id").description("user's id")),
                        links(halLinks(),
                                linkWithRel("self").description("self link"),
                                linkWithRel("update").description("link to update user"),
                                linkWithRel("delete").description("link to delete user")),
                        responseFields(
                                fieldWithPath("email").description("user's email"),
                                fieldWithPath("username").description("user's username"),
                                subsectionWithPath("_links").description("links to relative operations"))));

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
                .andExpect(jsonPath("$._links.self").hasJsonPath())
                .andDo(document("get-users/all/ok",
                        links(halLinks(),
                                linkWithRel("self").description("self link"),
                                linkWithRel("last").description("link to last page"),
                                linkWithRel("first").description("link to first page"),
                                linkWithRel("next").optional().description("link to next page if exists"),
                                linkWithRel("previous").optional().description("link to previous page if exists")),
                        responseFields(beneathPath("_embedded.userResponseList"),
                                fieldWithPath("email").description("user's email"),
                                fieldWithPath("username").description("user's username"),
                                subsectionWithPath("_links").description("links to relative operations")
                        ),
                        responseFields(beneathPath("page"),
                                fieldWithPath("size").description("user's email"),
                                fieldWithPath("totalElements").description("Total number of elements"),
                                fieldWithPath("totalPages").description("Total number of pages"),
                                fieldWithPath("number").description("Current page number"))));
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
                .andExpect(jsonPath("$.page.totalElements").value(FILTERED_COUNT))
                .andDo(document("get-users/filtered/ok", requestParameters(
                        parameterWithName("email").optional().description("Email matcher"),
                        parameterWithName("username").optional().description("Username matcher"))));
    }


    @Test
    public void getUserById_whenUserDoesNotExist_shouldReturnNotFound() throws Exception {
        final Long ID = 1L;

        Mockito.doThrow(EntityNotFoundException.class).when(userService).findUserById(anyLong());

        mockMvc.perform(get(USER_ID_PATH_TEMPLATE, ID)).andExpect(status().isNotFound())
                .andDo(document("get-user/error/not-found",
                        links(halLinks(),
                                linkWithRel("self").description("Self link")),
                        pathParameters(parameterWithName("id").description("user's id")),
                        responseFields(
                                fieldWithPath("code").description("Http response code"),
                                fieldWithPath("message").description("Error message"),
                                subsectionWithPath("_links").ignored()
                        )));
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
                .andExpect(status().isCreated())
                .andDo(document("create-user/ok",
                        requestFields(
                                fieldWithPath("username").description("User's username"),
                                fieldWithPath("email").description("User's email"),
                                fieldWithPath("password").description("user's password"))));
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
                .andExpect(jsonPath("$.errors[0].field").value("email"))
                .andDo(document("create-user/error/unprocessable-entity",
                        links(halLinks(),
                                linkWithRel("self").optional().description("Self link")),
                        responseFields(beneathPath("errors"),
                                fieldWithPath("field").description("Field with exception"),
                                fieldWithPath("message").description("Exception message"),
                                fieldWithPath("value").type(String.class).description("Invalid value")
                        )));
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
        final Long USER_ID = 1L;
        UserDto dto = new UserDto("testUsername", null, null);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String body = mapper.writeValueAsString(dto);

        mockMvc.perform(patch(USER_ID_PATH_TEMPLATE, USER_ID)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("update-user/ok",
                        requestFields(
                                fieldWithPath("username").optional().description("User's username"),
                                fieldWithPath("email").type(String.class).optional().description("User's email"))));

    }

    @Test
    public void updateUser_whenPasswordExist_shouldReturnUnprocessableEntity() throws Exception {
        final Long USER_ID = 1L;
        UserDto dto = new UserDto("testUsername", "mail@tester.cd", "asddfdsfdsf");
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String body = mapper.writeValueAsString(dto);

        mockMvc.perform(patch(USER_ID_PATH_TEMPLATE, USER_ID)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors[0].field").value("password"))
                .andDo(document("update-user/error/unprocessable-entity",
                        links(halLinks(),
                                linkWithRel("self").optional().description("Self link")),
                        responseFields(beneathPath("errors"),
                                fieldWithPath("field").description("Field with exception"),
                                fieldWithPath("message").optional().description("Exception message"),
                                fieldWithPath("value").type(String.class).optional().description("Invalid value")
                        )));
    }

    @Test
    public void deleteUser_whenUserExists_shouldReturnOk() throws Exception {
        final long ID = 1L;

        mockMvc.perform(delete(USER_ID_PATH_TEMPLATE, ID))
                .andExpect(status().isNoContent())
                .andDo(document("delete-user/ok",
                        pathParameters(parameterWithName("id").description("user's id"))));
    }

    @Test
    public void deleteUser_whenUserNotExists_shouldReturnNotFound() throws Exception {
        final long ID = -1;
        Mockito.doThrow(EntityNotFoundException.class).when(userService).deleteUser(anyLong());
        mockMvc.perform(delete(USER_ID_PATH_TEMPLATE, ID))
                .andExpect(status().isNotFound())
                .andDo(document("user/error/not-found",
                        links(halLinks(),
                                linkWithRel("self").description("Self link")),
                        pathParameters(parameterWithName("id").description("user's id")),
                        responseFields(
                                fieldWithPath("code").description("Http response code"),
                                fieldWithPath("message").description("Error message"),
                                subsectionWithPath("_links").description("redundant").ignored()
                        )));
    }


}