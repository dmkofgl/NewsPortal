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

    @Test(expected = ConstraintViolationException.class)
    public void addNews_whenTitleIsBlank_shouldTrowException() {
        NewsDto news = new NewsDto("  ", "content");
        newsService.createNews(news);
    }

    @Test
    public void addNews_whenNewsDtoIsvalid_shouldChangeSize() {
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
    public void findByTitleSpecifications() throws ParseException {
        final long COUNT_ALL = newsService.count();
        NewsSearchingDto titleDto = new NewsSearchingDto("test", null, null);
        PageRequest pageRequest = new PageRequest(0, 5);

        Page<News> userPageByTitle = newsService.getFilteredPage(titleDto, pageRequest);

        assertNotEquals(COUNT_ALL, userPageByTitle.getTotalElements());
        assertNotEquals(0L, userPageByTitle.getTotalElements());
    }

    @Test
    public void findByCreatedDateSpecifications() throws ParseException {
        final long COUNT_ALL = newsService.count();
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        dateFormat.applyPattern("yyyy-MM-dd");
        Date end = dateFormat.parse("2015-03-01");
        Date start = dateFormat.parse("2015-01-01");
        NewsSearchingDto dateDto = new NewsSearchingDto(null, start, end);
        PageRequest pageRequest = new PageRequest(0, 5);

        Page<News> userPageByEndDate = newsService.getFilteredPage(dateDto, pageRequest);

        assertNotEquals(COUNT_ALL, userPageByEndDate.getTotalElements());
        assertNotEquals(0L, userPageByEndDate.getTotalElements());
    }

    @Test
    public void findByEndCreatedDateSpecifications() throws ParseException {
        final long COUNT_ALL = newsService.count();
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        dateFormat.applyPattern("yyyy-MM-dd");
        Date end = dateFormat.parse("2015-03-01");
        NewsSearchingDto dateDto = new NewsSearchingDto(null, null, end);
        PageRequest pageRequest = new PageRequest(0, 5);

        Page<News> userPageByEndDate = newsService.getFilteredPage(dateDto, pageRequest);

        assertNotEquals(COUNT_ALL, userPageByEndDate.getTotalElements());
        assertNotEquals(0L, userPageByEndDate.getTotalElements());
    }

    @Test
    public void findByStartCreatedDateSpecifications() throws ParseException {
        final long COUNT_ALL = newsService.count();
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        dateFormat.applyPattern("yyyy-MM-dd");
        Date start = dateFormat.parse("2015-03-01");
        NewsSearchingDto startDateDto = new NewsSearchingDto(null, start, null);
        PageRequest pageRequest = new PageRequest(0, 5);

        Page<News> userPageByStartDate = newsService.getFilteredPage(startDateDto, pageRequest);

        assertNotEquals(COUNT_ALL, userPageByStartDate.getTotalElements());
        assertNotEquals(0L, userPageByStartDate.getTotalElements());
    }

    @Test
    public void findByCreatedDateAndTitleSpecifications() throws ParseException {
        final long COUNT_ALL = newsService.count();
        PageRequest pageRequest = new PageRequest(0, 5);
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        dateFormat.applyPattern("yyyy-MM-dd");
        Date end = dateFormat.parse("2015-03-01");
        NewsSearchingDto titleAndEndDateDto = new NewsSearchingDto("test", null, end);

        Page<News> userPageByTitleAndEndDate = newsService.getFilteredPage(titleAndEndDateDto, pageRequest);

        assertNotEquals(COUNT_ALL, userPageByTitleAndEndDate.getTotalElements());
        assertNotEquals(0L, userPageByTitleAndEndDate.getTotalElements());
    }
}