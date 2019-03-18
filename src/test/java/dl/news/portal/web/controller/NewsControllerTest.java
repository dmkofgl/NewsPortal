package dl.news.portal.web.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import dl.news.portal.domain.dto.NewsDto;
import dl.news.portal.domain.entity.News;
import dl.news.portal.domain.entity.User;
import dl.news.portal.domain.service.NewsService;
import dl.news.portal.domain.service.UserService;
import org.junit.Before;
import dl.news.portal.domain.service.UserService;
import dl.news.portal.web.config.security.jwt.JwtAuthenticationEntryPoint;
import dl.news.portal.web.config.security.jwt.TokenProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.restdocs.snippet.Snippet;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(NewsController.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
@WithMockUser(username = "name")
public class NewsControllerTest {
    private static final String NEWS_PATH = "/news";
    private static final String NEWS_ID_PATH_TEMPLATE = NEWS_PATH + "/{id}";
    private User defaultUser;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private NewsService newsService;
    @MockBean
    private UserService userService;
    @MockBean
    private UserDetailsService userDetailsService;
    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @MockBean
    private TokenProvider tokenProvider;
    @MockBean
    private PasswordEncoder passwordEncoder;

    @Before
    public void setUp() throws Exception {
        defaultUser = new User();
        defaultUser.setId(1L);
    }

    @Test
    public void getNewsById_whenNewsExists_shouldReturnOk() throws Exception {
        final Long ID = 1L;
        User user = new User();
        user.setId(2L);
        News news = new News();
        news.setId(1L);
        news.setContent("test");
        news.setTitle("test");
        news.setAuthor(user);

        Mockito.when(newsService.findNewsById(1L)).thenReturn(Optional.of(news));

        mockMvc.perform(get(NEWS_ID_PATH_TEMPLATE, ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("test"))
                .andExpect(jsonPath("$.content").value("test"))
                .andExpect(jsonPath("$._links.self").hasJsonPath())
                .andDo(document("get-news/single/ok",
                        pathParameters(parameterWithName("id").description("news's id")),
                        links(halLinks(),
                                linkWithRel("author").description("link to author of that news"),
                                linkWithRel("self").description("self link"),
                                linkWithRel("update").description("link to update that news"),
                                linkWithRel("delete").description("link to delete that news")),
                        responseFields(
                                fieldWithPath("title").description("News title"),
                                fieldWithPath("content").description("News content"),
                                fieldWithPath("updatedDate").type(Date.class).description("News last update date"),
                                fieldWithPath("createdDate").type(Date.class).description("Date when that news was saved first time."),
                                subsectionWithPath("_links").description("links to relative operations"))));
    }

    @Test
    public void getNews_whenDtoIsEmpty_shouldReturnOk() throws Exception {
        List<News> newsList = new ArrayList<>();
        News news = new News();
        news.setId(1L);
        news.setTitle("Title");
        news.setContent("This is content.");
        news.setAuthor(defaultUser);
        newsList.add(news);
        Page<News> userPage = new PageImpl<>(newsList);

        Mockito.when(newsService.getFilteredPage(any(NewsDto.class), any(Pageable.class))).thenReturn(userPage);

        mockMvc.perform(get(NEWS_PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.newsResponseList[0].title").value(news.getTitle()))
                .andExpect(jsonPath("$._embedded.newsResponseList[0].content").value(news.getContent()))
                .andExpect(jsonPath("$.page").hasJsonPath())
                .andExpect(jsonPath("$._links.self").hasJsonPath())
                .andDo(document("get-news/all/ok",
                        links(halLinks(),
                                linkWithRel("self").description("self link"),
                                linkWithRel("last").description("link to last page"),
                                linkWithRel("first").description("link to first page"),
                                linkWithRel("next").optional().description("link to next page if exists"),
                                linkWithRel("previous").optional().description("link to previous page if exists")),
                        responseFields(beneathPath("_embedded.newsResponseList"),
                                fieldWithPath("title").description("News title"),
                                fieldWithPath("content").description("News content"),
                                fieldWithPath("updatedDate").type(Date.class).description("News last update date"),
                                fieldWithPath("createdDate").type(Date.class).description("Date when that news was saved first time."),
                                subsectionWithPath("_links").description("links to relative operations")),
                        responseFields(beneathPath("page"),
                                fieldWithPath("size").description("Page size, default 20"),
                                fieldWithPath("totalElements").description("Total number of elements"),
                                fieldWithPath("totalPages").description("Total number of pages"),
                                fieldWithPath("number").description("Current page number"))));
    }

    @Test
    public void getNews_whenFindByTitle_shouldReturnOk() throws Exception {
        final String TITLE_MATCH = "tle";
        List<News> newsList = new ArrayList<>();
        News news = new News();
        news.setId(1L);
        news.setTitle("Title");
        news.setContent("This is content.");
        news.setAuthor(defaultUser);
        newsList.add(news);
        Page<News> newsPage = new PageImpl<>(newsList);

        Mockito.when(newsService.getFilteredPage(any(NewsDto.class), any(Pageable.class))).thenReturn(newsPage);

        mockMvc.perform(get(NEWS_PATH).param("title", TITLE_MATCH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.newsResponseList[0].title").value(news.getTitle()))
                .andExpect(jsonPath("$._embedded.newsResponseList[0].content").value(news.getContent()))
                .andExpect(jsonPath("$.page").hasJsonPath())
                .andExpect(jsonPath("$._links.self").hasJsonPath())
                .andDo(document("get-news/filtered/ok",
                        requestParameters(
                                parameterWithName("title").optional().description("Title matcher"),
                                parameterWithName("createDate").optional().description("Find all news created after that date"),
                                parameterWithName("endCreateDate").optional().description("Find all news created before that date"))));
    }

    @Test
    public void getNewsById_whenNewsDoesNotExist_shouldReturnNotFound() throws Exception {
        final Long ID = 1L;
        Mockito.doThrow(EntityNotFoundException.class).when(newsService).findNewsById(anyLong());
        mockMvc.perform(get(NEWS_ID_PATH_TEMPLATE, ID)).andExpect(status().isNotFound())
                .andDo(document("get-news/single/error/not-found", newsNotFoundSnippet()));
    }

    @Test
    public void addNews_whenNewsDtoIsValid_shouldReturnCreated() throws Exception {
        NewsDto news = new NewsDto("test", "test");
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String body = mapper.writeValueAsString(news);
        mockMvc.perform(post(NEWS_PATH)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(document("create-news/ok",
                        requestFields(
                                fieldWithPath("title").description("News title"),
                                fieldWithPath("content").description("News content"))));
    }

    @Test
    public void addNews_whenNewsDtoWithNullField_shouldReturnUnprocessableEntity() throws Exception {
        NewsDto news = new NewsDto(null, "test");
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String body = mapper.writeValueAsString(news);

        mockMvc.perform(post(NEWS_PATH)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors.[0].field").value("title"))
                .andExpect(jsonPath("$._links.self").hasJsonPath())
                .andDo(document("create-news/error/unprocessable-entity",
                        links(halLinks(),
                                linkWithRel("self").optional().description("Self link")),
                        responseFields(beneathPath("errors"),
                                fieldWithPath("field").description("Field with exception"),
                                fieldWithPath("message").type(String.class).description("Exception message"),
                                fieldWithPath("value").type(String.class).description("Invalid value"))));
    }


    @Test
    public void addNews_whenNewsDtoFieldIsNotValid_shouldReturnUnprocessableEntity() throws Exception {
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
    public void updateNews_whenNewsDtoWithNullField_shouldReturnOk() throws Exception {
        final Long ID = 1L;
        NewsDto dto = new NewsDto(null, "new content");
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String body = mapper.writeValueAsString(dto);

        mockMvc.perform(patch(NEWS_ID_PATH_TEMPLATE, ID)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void updateNews_whenNewsDtoIsValid_shouldReturnOk() throws Exception {
        final Long ID = 1L;
        NewsDto dto = new NewsDto("newTitle", "new content");
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String body = mapper.writeValueAsString(dto);

        mockMvc.perform(patch(NEWS_ID_PATH_TEMPLATE, ID)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("update-news/ok",
                        requestFields(
                                fieldWithPath("title").optional().description("New title for that news."),
                                fieldWithPath("content").optional().description("New content for that news."))));
    }

    @Test
    public void updateNews_whenNewsNotExists_shouldReturnNotFound() throws Exception {
        final long ID = -1;
        NewsDto dto = new NewsDto("newTitle", "new content");
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String body = mapper.writeValueAsString(dto);

        Mockito.doThrow(EntityNotFoundException.class).when(newsService).updateNews(anyLong(), any(NewsDto.class));

        mockMvc.perform(patch(NEWS_ID_PATH_TEMPLATE, ID)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(document("update-news/error/not-found", newsNotFoundSnippet()));
    }

    @Test
    public void deleteNews_whenNewsExists_shouldReturnOk() throws Exception {
        final long ID = 1L;

        mockMvc.perform(delete(NEWS_ID_PATH_TEMPLATE, ID))
                .andExpect(status().isNoContent())
                .andDo(document("delete-news/ok",
                        pathParameters(parameterWithName("id").description("News' id"))));
    }

    @Test
    public void deleteNews_whenNewsNotExists_shouldReturnNotFound() throws Exception {
        final long ID = -1;

        Mockito.doThrow(EntityNotFoundException.class).when(newsService).deleteById(anyLong());

        mockMvc.perform(delete(NEWS_ID_PATH_TEMPLATE, ID))
                .andExpect(status().isNotFound())
                .andDo(document("delete-news/error/not-found", newsNotFoundSnippet()));
    }

    private Snippet[] newsNotFoundSnippet() {
        return new Snippet[]{
                links(halLinks(),
                        linkWithRel("self").description("Self link")),
                pathParameters(parameterWithName("id").description("News' id")),
                responseFields(
                        fieldWithPath("code").description("Http response code"),
                        fieldWithPath("message").type(String.class).description("Error message"),
                        subsectionWithPath("_links").ignored()
                )};
    }
}