package dl.news.portal.domain.dto;

@FunctionalInterface
public interface DtoTransofrm<T> {
    T transform();
}
