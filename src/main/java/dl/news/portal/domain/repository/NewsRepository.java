package dl.news.portal.domain.repository;

import dl.news.portal.domain.entity.News;
import dl.news.portal.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    List<News> findByTitleIgnoreCaseContaining(String title);

    List<News> findByAuthor(User user);

    List<News> findByAuthorId(Long authorId);

    List<News> findByUpdatedDateBetween(Date start, Date end);
}
