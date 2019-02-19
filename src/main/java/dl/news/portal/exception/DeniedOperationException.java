package dl.news.portal.exception;

public class DeniedOperationException extends RuntimeException {
    public DeniedOperationException(String message) {
        super(message);
    }

    public DeniedOperationException() {
    }
}
