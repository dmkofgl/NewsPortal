package dl.news.portal.domain.service;

import dl.news.portal.domain.dto.DtoTransfer;
import dl.news.portal.domain.entity.News;
import dl.news.portal.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Optional;


public interface NewsService {
    Page<News> getNewsPage(Pageable pageable);

    List<News> getAllNews();

    Optional<News> findNewsById(Long id);

    void createNews(News news);

    void updateNews(Long id, DtoTransfer<News> news);

    void deleteById(Long id);

    List<News> findByTitle(String title);

    List<News> findByAuthor(User user);

    Page<News> findPageByAuthor(User user, Pageable pageable);

    List<News> findByUpdatedDate(Date start, Date end);
}
