package dl.news.portal.web.controller;

import dl.news.portal.domain.resource.exception.BindExceptionResource;
import dl.news.portal.domain.resource.exception.EntityNotExistsResource;
import dl.news.portal.domain.resource.exception.ValidateExceptionResource;
import dl.news.portal.exception.DeniedOperationException;
import dl.news.portal.exception.EntityNotExistsException;
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
    public ResponseEntity<List<ValidateExceptionResource>> handleViolationException(ConstraintViolationException ex) {
        List<ValidateExceptionResource> resources = ex.getConstraintViolations()
                .stream().map(ValidateExceptionResource::new).collect(Collectors.toList());
        return new ResponseEntity<>(resources, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler({EntityNotExistsException.class})
    public ResponseEntity<EntityNotExistsResource> handleEntityNotFoundException(EntityNotExistsException ex) {
        EntityNotExistsResource resources = new EntityNotExistsResource(ex);
        return new ResponseEntity<>(resources, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({DeniedOperationException.class})
    public ResponseEntity<Resource<String>> handleDeniedOperationException(DeniedOperationException ex) {
        Resource<String> resource = new Resource<>(ex.getMessage());
        return new ResponseEntity<>(resource, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<Resource<String>> handleEntityNotFoundException(EntityNotFoundException ex) {
        Resource<String> resource = new Resource<>(ex.getMessage());
        return new ResponseEntity<>(resource, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        BindExceptionResource resource = new BindExceptionResource(ex);
        return new ResponseEntity<>(resource, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        BindExceptionResource resource = new BindExceptionResource(ex.getBindingResult());
        return new ResponseEntity<>(resource, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
