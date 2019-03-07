package dl.news.portal.web.exceptionHandler;

import com.fasterxml.jackson.core.io.JsonEOFException;
import dl.news.portal.domain.response.exception.BindExceptionResponse;
import dl.news.portal.domain.response.exception.ErrorResponse;
import dl.news.portal.domain.response.exception.ValidateExceptionResponse;
import dl.news.portal.exception.DeniedParameterException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<List<ValidateExceptionResponse>> handleViolationException(ConstraintViolationException ex) {
        List<ValidateExceptionResponse> resources = ex.getConstraintViolations()
                .stream().map(ValidateExceptionResponse::new).collect(Collectors.toList());
        return new ResponseEntity<>(resources, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
        ErrorResponse response = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.getReasonPhrase());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({DeniedParameterException.class})
    public ResponseEntity<Resource<String>> handleDeniedDeniedParameterException(DeniedParameterException ex) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        BindExceptionResponse resource = new BindExceptionResponse(ex);
        return new ResponseEntity<>(resource, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        BindExceptionResponse resource = new BindExceptionResponse(ex.getBindingResult());
        return new ResponseEntity<>(resource, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler({ExpiredJwtException.class})
    public ResponseEntity<ErrorResponse> handleExpiredJwtException(ExpiredJwtException ex) {
        ErrorResponse resources = new ErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        return new ResponseEntity<>(resources, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({SignatureException.class})
    public ResponseEntity<ErrorResponse> handleSignatureException(SignatureException ex) {
        ErrorResponse resources = new ErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        return new ResponseEntity<>(resources, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({JsonEOFException.class})
    public ResponseEntity<ErrorResponse> handleSignatureException(JsonEOFException ex) {
        ErrorResponse resources = new ErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        return new ResponseEntity<>(resources, HttpStatus.UNAUTHORIZED);
    }


}
