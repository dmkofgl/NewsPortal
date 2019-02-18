package dl.news.portal.domain.dto;

@FunctionalInterface
public interface DtoTransfer<T> {
    void transfer(T receiver);
}
