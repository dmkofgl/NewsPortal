package dl.news.portal.domain.dto;

import dl.news.portal.domain.entity.News;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.criteria.Predicate;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;

public class NewsSearchingDto implements DtoTransform<News>, DtoTransfer<News>, SearchingSpecification<News> {
    private Optional<String> title;
    private Optional<Date> createDate;
    private Optional<Date> endCreateDate;

    public Optional<String> getTitle() {
        return title;
    }

    public Optional<Date> getEndCreateDate() {
        return endCreateDate;
    }

    public Optional<Date> getCreateDate() {
        return createDate;
    }

    public void setEndCreateDate(Date endCreateDate) {
        this.endCreateDate = Optional.ofNullable(endCreateDate);
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public void setCreateDate(Date date) {
        this.createDate = Optional.ofNullable(date);
    }

    public void setTitle(String title) {
        this.title = Optional.ofNullable(title);
    }


    public NewsSearchingDto(String title, Date CreateDate, Date endCreateDate) {
        this.title = Optional.ofNullable(title);
        this.createDate = Optional.ofNullable(CreateDate);
        this.endCreateDate = Optional.ofNullable(endCreateDate);
    }

    public NewsSearchingDto() {
        this(null, null, null);
    }

    @Override
    public void transfer(News receiver) {
        title.ifPresent(receiver::setTitle);
        createDate.ifPresent(receiver::setCreatedDate);
    }

    @Override
    public News transform() {
        News news = new News();
        transfer(news);
        return news;
    }

    @Override
    public Specification<News> getSpecification() {
        return (root, criteriaQuery, criteriaBuilder) -> {
            Collection<Predicate> predicates = new HashSet<>();
            title.ifPresent(u -> predicates.add(criteriaBuilder.like(root.get("title"), "%" + u + "%")));
            createDate.ifPresent(u -> predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate"), u)));
            endCreateDate.ifPresent(u -> predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdDate"), u)));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

