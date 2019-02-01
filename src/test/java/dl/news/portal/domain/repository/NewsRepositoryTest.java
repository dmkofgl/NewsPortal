package dl.news.portal.domain.repository;

import dl.news.portal.domain.entity.News;
import dl.news.portal.web.config.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
public class NewsRepositoryTest {
    @Autowired
    private NewsRepository newsRepository;

    @Test
    public void findById() {
        Optional<News> newsOptional = newsRepository.findById(1L);
        assertTrue(newsOptional.isPresent());
    }

    @Test
    public void findByTitle() {
        List<News> news = newsRepository.findByTitleIgnoreCaseContaining("Test");
        List<News> newsByUpper = newsRepository.findByTitleIgnoreCaseContaining("TeST");
        assertTrue(news.size() != 0);
        assertTrue(newsByUpper.size() != 0);
    }
}