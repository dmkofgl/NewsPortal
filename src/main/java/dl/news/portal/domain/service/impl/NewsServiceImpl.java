package dl.news.portal.domain.service.impl;

import dl.news.portal.domain.dto.NewsDto;
import dl.news.portal.domain.entity.News;
import dl.news.portal.domain.repository.NewsRepository;
import dl.news.portal.domain.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.*;

@Service
public class NewsServiceImpl implements NewsService {
    @Autowired
    private NewsRepository newsRepository;

    @Override
    public Page<News> getFilteredPage(NewsDto dto, Pageable pageable) {
        Specification<News> specification = getSearchingSpecification(dto);
        return newsRepository.findAll(specification, pageable);
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
    public void createNews(NewsDto dto) {
        News news = new News();
        transformDto(news, dto);
        newsRepository.save(news);
    }

    @Override
    public void updateNews(Long id, NewsDto dto) {
        News news = newsRepository.getOne(id);
        transformDto(news, dto);
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

    private void transformDto(News receiver, NewsDto dto) {
        String title = dto.getTitle();
        String content = dto.getContent();

        if (title != null) {
            receiver.setTitle(title);
        }

        if (content != null) {
            receiver.setContent(content);
        }
    }

    private Specification<News> getSearchingSpecification(NewsDto dto) {
        String title = dto.getTitle();
        Date createDate = dto.getCreateDate();
        Date endCreateDate = dto.getEndCreateDate();
        return (root, criteriaQuery, criteriaBuilder) -> {
            Collection<Predicate> predicates = new HashSet<>();
            if (title != null) {
                predicates.add(criteriaBuilder.like(root.get("title"), "%" + title + "%"));
            }
            if (createDate != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate"), createDate));
            }
            if (endCreateDate != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdDate"), endCreateDate));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
