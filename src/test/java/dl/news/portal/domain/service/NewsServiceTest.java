package dl.news.portal.domain.service;

import dl.news.portal.domain.dto.NewsDto;
import dl.news.portal.domain.dto.NewsSearchingDto;
import dl.news.portal.domain.entity.News;
import dl.news.portal.domain.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class NewsServiceTest {
    @Autowired
    private NewsService newsService;

    @Test
    public void findById() {
        Optional<News> newsOptional = newsService.findNewsById(1L);
        assertTrue(newsOptional.isPresent());
    }


    @Test
    public void findByTitle() {
        List<News> news = newsService.findByTitle("Test");
        List<News> newsByUpper = newsService.findByTitle("TeST");
        assertEquals(news.size(), newsByUpper.size());
    }

    @Test(expected = ConstraintViolationException.class)
    public void addNewsWithAuthorException() {
        News news = new News();
        news.setTitle("test title");
        news.setContent("content");
        newsService.createNews(news);
    }

    @Test
    public void addNews() {
        User user = new User();
        user.setId(1L);
        News news = new News();
        news.setTitle("test title");
        news.setContent("content");
        news.setAuthor(user);
        newsService.createNews(news);
        assertNotNull(news.getId());
    }

    @Test
    public void updateNews() {
        final String newContent = "new content";
        NewsDto updatedNews = new NewsDto(null, newContent);
        newsService.updateNews(1L, updatedNews);
        News news = newsService.findNewsById(1L).get();
        assertEquals(newContent, news.getContent());
    }

    @Test
    public void findByAuthor() {
        User user = new User();
        user.setId(1L);
        List<News> news = newsService.findByAuthor(user);
        assertNotEquals(0, news.size());
    }

    @Test
    public void findByUpdatedDateBetween() {
        Date end = new Date();
        Date start = new Date(end.getTime() - 31536000000L);

        List<News> news = newsService.findByUpdatedDate(start, end);
        List<News> allNews = newsService.getAllNews();
        assertNotEquals(allNews.size(), news.size());
    }

    @Test
    public void findBySpecifications() throws ParseException {
        final long COUNT_ALL = newsService.count();
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        dateFormat.applyPattern("yyyy-MM-dd");
        Date end = dateFormat.parse("2015-03-01");
        NewsSearchingDto endDateDto = new NewsSearchingDto(null, null, end);
        NewsSearchingDto titleDto = new NewsSearchingDto("test", null, null);
        NewsSearchingDto titleAndEndDateDto = new NewsSearchingDto("test", null, end);
        PageRequest pageRequest = new PageRequest(0, 5);

        Page<News> userPageByTitle = newsService.getFilteredPage(titleDto, pageRequest);
        Page<News> userPageByEndDate = newsService.getFilteredPage(endDateDto, pageRequest);
        Page<News> userPageByTitleAndEndDate = newsService.getFilteredPage(titleAndEndDateDto, pageRequest);

        assertNotEquals(COUNT_ALL, userPageByTitle.getTotalElements());
        assertNotEquals(0L, userPageByTitle.getTotalElements());
        assertNotEquals(COUNT_ALL, userPageByEndDate.getTotalElements());
        assertNotEquals(0L, userPageByEndDate.getTotalElements());
        assertNotEquals(COUNT_ALL, userPageByTitleAndEndDate.getTotalElements());
        assertNotEquals(0L, userPageByTitleAndEndDate.getTotalElements());
    }
}