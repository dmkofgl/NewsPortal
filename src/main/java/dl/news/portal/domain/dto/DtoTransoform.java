package dl.news.portal.domain.dto;

@FunctionalInterface
public interface DtoTransoform<T> {
    T transform();
}
