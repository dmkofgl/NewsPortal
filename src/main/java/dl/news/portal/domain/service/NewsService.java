package dl.news.portal.domain.service;

import dl.news.portal.domain.dto.NewsDto;
import dl.news.portal.domain.entity.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


public interface NewsService {

    Optional<News> findNewsById(Long id);

    void createNews(NewsDto news);

    void updateNews(Long id, NewsDto news);

    void deleteById(Long id);

    Page<News> getFilteredPage(NewsDto dto, Pageable pageable);

    List<News> getAllNews();

    Long count();
}
