package dl.news.portal.domain.service.impl;

import dl.news.portal.domain.dto.DtoTransfer;
import dl.news.portal.domain.dto.SearchingSpecification;
import dl.news.portal.domain.entity.News;
import dl.news.portal.domain.entity.User;
import dl.news.portal.domain.repository.NewsRepository;
import dl.news.portal.domain.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class NewsServiceImpl implements NewsService {
    @Autowired
    private NewsRepository newsRepository;

    @Override
    public Page<News> getNewsPage(Pageable pageable) {
        return newsRepository.findAll(pageable);
    }

    @Override
    public List<News> getAllNews() {
        return newsRepository.findAll();
    }

    @Override
    public Optional<News> findNewsById(Long id) {
        return newsRepository.findById(id);
    }

    @Override
    public void createNews(News news) {
        newsRepository.save(news);
    }

    @Override
    public void updateNews(Long id, DtoTransfer<News> dtoTransfer) {
        News news = newsRepository.getOne(id);
        dtoTransfer.transfer(news);
        newsRepository.save(news);
    }

    @Override
    public void deleteById(Long id) {
        newsRepository.deleteById(id);
    }

    @Override
    public List<News> findByTitle(String title) {
        return newsRepository.findByTitleIgnoreCaseContaining(title);
    }

    @Override
    public List<News> findByAuthor(User user) {
        return newsRepository.findByAuthor(user);
    }

    @Override
    public Page<News> findPageByAuthor(User user, Pageable pageable) {
        return newsRepository.findPageByAuthor(user, pageable);
    }

    @Override
    public List<News> findByUpdatedDate(Date start, Date end) {
        return newsRepository.findByUpdatedDateBetween(start, end);
    }

    @Override
    public Page<News> getFilteredPage(SearchingSpecification<News> dto, Pageable pageable) {
        Specification<News> specification = dto.getSpecification();
        return newsRepository.findAll(specification, pageable);
    }

    @Override
    public Long count() {
        return newsRepository.count();
    }
}
