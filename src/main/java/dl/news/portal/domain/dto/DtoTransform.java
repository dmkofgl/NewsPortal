package dl.news.portal.domain.dto;

@FunctionalInterface
public interface DtoTransform<T> {
    T transform();
}
