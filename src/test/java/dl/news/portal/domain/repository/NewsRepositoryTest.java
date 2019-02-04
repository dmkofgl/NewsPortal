package dl.news.portal.domain.repository;

import dl.news.portal.domain.entity.News;
import dl.news.portal.domain.entity.User;
import dl.news.portal.web.config.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
@Transactional
public class NewsRepositoryTest {
    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void findById() {
        Optional<News> newsOptional = newsRepository.findById(1L);
        assertTrue(newsOptional.isPresent());
    }


    @Test
    public void findByTitle() {
        List<News> news = newsRepository.findByTitleIgnoreCaseContaining("Test");
        List<News> newsByUpper = newsRepository.findByTitleIgnoreCaseContaining("TeST");
        assertEquals(news.size(), newsByUpper.size());
    }

    @Test(expected = ConstraintViolationException.class)
    public void addNewsWithAuthorException() {
        News news = new News();
        news.setTitle("test title");
        news.setContent("content");
        newsRepository.save(news);
    }

    @Test
    public void addNews() {
        User user = userRepository.findById(1L).get();
        News news = new News();
        news.setTitle("test title");
        news.setContent("content");
        news.setAuthor(user);
        newsRepository.save(news);
        assertNotNull(news.getId());
    }

    @Test
    public void findByAuthor() {
        User user = userRepository.findById(1L).get();
        List<News> news = newsRepository.findByAuthor(user);
        assertNotEquals(0, news.size());
    }
}