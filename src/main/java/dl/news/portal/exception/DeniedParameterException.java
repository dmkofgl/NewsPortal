package dl.news.portal.exception;

public class DeniedParameterException extends RuntimeException {
    public DeniedParameterException(String message) {
        super(message);
    }

    public DeniedParameterException() {
    }
}
