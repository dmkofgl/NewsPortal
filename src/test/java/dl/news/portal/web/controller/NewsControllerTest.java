package dl.news.portal.web.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import dl.news.portal.domain.dto.NewsDto;
import dl.news.portal.domain.entity.News;
import dl.news.portal.domain.entity.User;
import dl.news.portal.domain.service.NewsService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(NewsController.class)
public class NewsControllerTest {
    private static final String NEWS_PATH = "/news";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private NewsService newsService;
    @MockBean
    private UserService userService;


    @Test
    public void getNewsById_whenNewsExists_shouldReturnOk() throws Exception {
        final String path = NEWS_PATH + "/1";
        User user = new User();
        user.setId(2L);
        News news = new News();
        news.setId(1L);
        news.setContent("test");
        news.setTitle("test");
        news.setAuthor(user);
        Mockito.when(newsService.findNewsById(1L)).thenReturn(Optional.of(news));
        mockMvc.perform(get(path))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("test"))
                .andExpect(jsonPath("$.content").value("test"))
                .andExpect(jsonPath("$._links.self").hasJsonPath());

    }

    @Test
    public void getNewsById_whenNewsDoesNotExist_shouldReturnNotFound() throws Exception {
        final String path = NEWS_PATH + "/1";
        mockMvc.perform(get(path)).andExpect(status().isNotFound());
    }

    @Test
    public void addNews_whenNewsDtoIsValid_shouldReturnCreated() throws Exception {
        User user = new User();
        user.setId(2L);
        NewsDto news = new NewsDto("test", "test");
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String body = mapper.writeValueAsString(news);
        mockMvc.perform(post("/news")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void addNews_whenNewsDtoWithNullField_shouldReturnUnprocessableEntity() throws Exception {
        User user = new User();
        user.setId(2L);
        NewsDto news = new NewsDto(null, "test");
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String body = mapper.writeValueAsString(news);

        mockMvc.perform(post(NEWS_PATH)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors.[0].field").value("title"))
                .andExpect(jsonPath("$._links.self").hasJsonPath());
    }

    @Test
    public void addNews_whenNewsDtoFieldIsNotValid_shouldReturnUnprocessableEntity() throws Exception {
        User user = new User();
        user.setId(2L);
        NewsDto news = new NewsDto("   ", "test");
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String body = mapper.writeValueAsString(news);

        mockMvc.perform(post(NEWS_PATH)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors.[0].field").value("title"))
                .andExpect(jsonPath("$.errors.[0].value").value("   "))
                .andExpect(jsonPath("$._links.self").hasJsonPath());
    }

    @Test
    public void updateUser_whenUserDtoWithNullField_shouldReturnOk() throws Exception {
        final String path = NEWS_PATH + "/1";
        NewsDto dto = new NewsDto(null, "new content");
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String body = mapper.writeValueAsString(dto);

        mockMvc.perform(patch(path)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void updateUser_whenUserDtoIsValid_shouldReturnOk() throws Exception {
        final String path = NEWS_PATH + "/1";
        NewsDto dto = new NewsDto("newTitle", "new content");
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String body = mapper.writeValueAsString(dto);

        mockMvc.perform(patch(path)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}