package dl.news.portal.domain.repository;

import dl.news.portal.domain.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    List<News> findByTitleIgnoreCaseContaining(String title);
}
