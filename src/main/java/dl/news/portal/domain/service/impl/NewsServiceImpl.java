package dl.news.portal.domain.service.impl;

import dl.news.portal.domain.dto.NewsDto;
import dl.news.portal.domain.dto.SearchingSpecification;
import dl.news.portal.domain.entity.News;
import dl.news.portal.domain.repository.NewsRepository;
import dl.news.portal.domain.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NewsServiceImpl implements NewsService {
    @Autowired
    private NewsRepository newsRepository;

    @Override
    public Page<News> getFilteredPage(SearchingSpecification<News> dto, Pageable pageable) {
        Specification<News> specification = dto.getSpecification();
        return newsRepository.findAll(specification, pageable);
    }

    @Override
    public Optional<News> findNewsById(Long id) {
        return newsRepository.findById(id);
    }

    @Override
    public void createNews(NewsDto dto) {
        News news = new News();
        transferDto(news, dto);
        newsRepository.save(news);
    }

    @Override
    public void updateNews(Long id, NewsDto dto) {
        News news = newsRepository.getOne(id);
        transferDto(news, dto);
        newsRepository.save(news);
    }

    @Override
    public void deleteById(Long id) {
        newsRepository.deleteById(id);
    }

    @Override
    public Long count() {
        return newsRepository.count();
    }

    private void transferDto(News receiver, NewsDto dto) {
        String title = dto.getTitle();
        String content = dto.getContent();
        if (title != null) {
            receiver.setTitle(title);
        }
        if (content != null) {
            receiver.setContent(content);
        }
    }
}
