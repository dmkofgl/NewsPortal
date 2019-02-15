package dl.news.portal.domain.dto;

import org.springframework.data.jpa.domain.Specification;

public interface SearchingSpecification<T> {
     Specification<T> getSpecification();
}
