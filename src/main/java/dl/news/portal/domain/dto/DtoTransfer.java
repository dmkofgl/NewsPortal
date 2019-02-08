package dl.news.portal.domain.dto;

public interface DtoTransfer<T> {
    void transfer(T receiver);
}
