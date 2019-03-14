package dl.news.portal.domain.service;

import dl.news.portal.domain.dto.NewsDto;
import dl.news.portal.domain.dto.NewsDtoBuilder;
import dl.news.portal.domain.entity.News;
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
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class NewsServiceTest {
    @Autowired
    private NewsService newsService;

    @Test
    public void findById_whenNewsExists_shouldBePresent() {
        Optional<News> newsOptional = newsService.findNewsById(1L);
        assertTrue(newsOptional.isPresent());
    }

    @Test(expected = ConstraintViolationException.class)
    public void addNews_whenTitleIsBlank_shouldTrowException() {
        NewsDto news = new NewsDto("  ", "content");
        newsService.createNews(news);
    }

    @Test
    public void addNews_whenNewsDtoIsValid_shouldChangeSize() {
        Long size = newsService.count();
        NewsDto news = new NewsDto("test title", "content");
        newsService.createNews(news);
        assertNotEquals(size, newsService.count());
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
    public void updateNewsContent_whenTitleIsBlank_contentsShouldBeEquals() {
        final String NEW_CONTENT = "new content";
        final String NEW_TITLE = "    ";
        NewsDto updatedNews = new NewsDto(NEW_TITLE, NEW_CONTENT);
        newsService.updateNews(1L, updatedNews);
        News news = newsService.findNewsById(1L).get();
        assertEquals(NEW_CONTENT, news.getContent());
        assertNotEquals(NEW_TITLE, news.getTitle());
    }

    @Test
    public void findBySpecifications_whenFilterOnlyTitle_shouldCountEqual() throws ParseException {
        final String TITLE = "test";
        final long COUNT = newsService.getAllNews().stream().filter(n -> n.getTitle().contains(TITLE)).count();

        NewsDto titleDto = new NewsDtoBuilder().withTitle(TITLE).build();
        PageRequest pageRequest = new PageRequest(0, 5);

        Page<News> userPageByTitle = newsService.getFilteredPage(titleDto, pageRequest);

        assertEquals(COUNT, userPageByTitle.getTotalElements());
    }

    @Test
    public void findBySpecifications_whenFilterByCreatedDate_shouldCountEqual() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        dateFormat.applyPattern("yyyy-MM-dd");
        Date end = dateFormat.parse("2015-03-01");
        Date start = dateFormat.parse("2015-01-01");
        final long COUNT = newsService.getAllNews().stream()
                .filter(n -> n.getCreatedDate().compareTo(start) >= 0)
                .filter(n -> n.getCreatedDate().compareTo(end) <= 0)
                .count();

        NewsDto dateDto = new NewsDtoBuilder()
                .withStartCreatedDate(start)
                .withEndCreatedDate(end)
                .build();
        PageRequest pageRequest = new PageRequest(0, 5);

        Page<News> userPageByEndDate = newsService.getFilteredPage(dateDto, pageRequest);

        assertEquals(COUNT, userPageByEndDate.getTotalElements());
    }

    @Test
    public void findBySpecifications_whenFilterByEndCreatedDate_shouldCountEqual() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        dateFormat.applyPattern("yyyy-MM-dd");
        Date end = dateFormat.parse("2015-03-01");
        final long COUNT = newsService.getAllNews().stream()
                .filter(n -> n.getCreatedDate().compareTo(end) <= 0)
                .count();
        NewsDto dateDto = new NewsDtoBuilder().withEndCreatedDate(end).build();
        PageRequest pageRequest = new PageRequest(0, 5);

        Page<News> userPageByEndDate = newsService.getFilteredPage(dateDto, pageRequest);

        assertEquals(COUNT, userPageByEndDate.getTotalElements());
    }

    @Test
    public void findBySpecifications_whenFilterByStartCreatedDate_shouldCountEqual() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        dateFormat.applyPattern("yyyy-MM-dd");
        Date start = dateFormat.parse("2015-03-01");
        final long COUNT = newsService.getAllNews().stream()
                .filter(n -> n.getCreatedDate().compareTo(start) >= 0)
                .count();
        NewsDto startDateDto = new NewsDtoBuilder().withStartCreatedDate(start).build();
        PageRequest pageRequest = new PageRequest(0, 5);

        Page<News> userPageByStartDate = newsService.getFilteredPage(startDateDto, pageRequest);

        assertEquals(COUNT, userPageByStartDate.getTotalElements());
    }

    @Test
    public void findBySpecifications_whenFilterByTitleAndEndCreatedDate_shouldCountEqual() throws ParseException {
        final String TITLE = "test";
        PageRequest pageRequest = new PageRequest(0, 5);
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        dateFormat.applyPattern("yyyy-MM-dd");
        Date end = dateFormat.parse("2015-03-01");
        final long COUNT = newsService.getAllNews().stream()
                .filter(n -> n.getCreatedDate().compareTo(end) <= 0)
                .filter(n -> n.getTitle().contains(TITLE))
                .count();
        NewsDto titleAndEndDateDto = new NewsDtoBuilder()
                .withTitle(TITLE)
                .withEndCreatedDate(end)
                .build();

        Page<News> userPageByTitleAndEndDate = newsService.getFilteredPage(titleAndEndDateDto, pageRequest);

        assertEquals(COUNT, userPageByTitleAndEndDate.getTotalElements());
    }
}