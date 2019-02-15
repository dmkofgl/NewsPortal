package dl.news.portal.web.exceptionHandler;

import dl.news.portal.exception.EntityNotExistsException;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
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

    class BindExceptionResource extends ResourceSupport {
        class BindObjectErrorResource extends ResourceSupport {
            private FieldError objectError;

            public String getField() {
                return objectError.getField();
            }

            public String getValue() {
                return objectError.getRejectedValue().toString();
            }

            public String getMessage() {
                return objectError.getDefaultMessage();
            }

            public BindObjectErrorResource(ObjectError objectError) {
                this.objectError = (FieldError) objectError;
            }
        }

        private BindingResult bindException;

        public List<BindObjectErrorResource> getErrors() {
            return bindException.getAllErrors().stream().map(BindObjectErrorResource::new).collect(Collectors.toList());
        }

        public BindExceptionResource(BindingResult bindException) {
            this.bindException = bindException;
            addSelfLink();
        }

        private void addSelfLink() {
            String path = createUri();
            Link link = new Link(path, Link.REL_SELF);
            add(link);
        }

        private String createUri() {
            return ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString();
        }
    }

    class ValidateExceptionResource extends ResourceSupport {
        private ConstraintViolation constraintViolation;

        public String getPath() {
            return constraintViolation.getPropertyPath().toString();
        }

        public String getMessage() {
            return constraintViolation.getMessage();
        }

        public ValidateExceptionResource(ConstraintViolation constraintViolation) {
            this.constraintViolation = constraintViolation;
            addSelfLink();
        }

        private void addSelfLink() {
            String path = createUri();
            Link link = new Link(path, Link.REL_SELF);
            add(link);
        }

        private String createUri() {
            return ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString();
        }
    }

    class EntityNotExistsResource extends ResourceSupport {
        private EntityNotExistsException exception;

        public String getEntityName() {
            return exception.getEntityName();
        }

        public String getMessage() {
            return exception.getMessage();
        }

        public EntityNotExistsResource(EntityNotExistsException exception) {
            this.exception = exception;
            addSelfLink();
        }

        private void addSelfLink() {
            String path = createUri();
            Link link = new Link(path, Link.REL_SELF);
            add(link);
        }

        private String createUri() {
            return ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString();
        }
    }

}
