package dl.news.portal.domain.service;

import dl.news.portal.domain.dto.NewsDto;
import dl.news.portal.domain.entity.News;
import dl.news.portal.domain.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
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
    public void addNews_whenTitleIsBlank_shouldTrowException() {
        NewsDto news = new NewsDto("  ", "content");
        newsService.createNews(news);
    }

    @Test
    public void addNews_whenNewsDtoIsValid_shouldChangeSize() {
        int size = newsService.getAllNews().size();
        NewsDto news = new NewsDto("test title", "content");
        newsService.createNews(news);
        assertNotEquals(size, newsService.getAllNews().size());
    }

    @Test
    public void updateNewsContent_whenTitleIsNull_contentsShouldBeEquals() {
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
}