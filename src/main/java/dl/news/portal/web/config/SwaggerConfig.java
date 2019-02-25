package dl.news.portal.web.config;

import com.fasterxml.classmate.TypeResolver;
import dl.news.portal.domain.response.exception.BindExceptionResponse;
import dl.news.portal.domain.response.exception.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.*;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.schema.AlternateTypeRuleConvention;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static springfox.documentation.schema.AlternateTypeRules.newRule;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Autowired
    private TypeResolver resolve;

    @Bean
    public Docket apiDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.POST, concatenatedList(messageCreated(), messageUnprocessableEntity(), messageNotFound()))
                .globalResponseMessage(RequestMethod.PATCH, concatenatedList(messageUnprocessableEntity(), messageNotFound()))
                .globalResponseMessage(RequestMethod.DELETE, concatenatedList(messageNoContent(), messageNotFound()))
                .additionalModels(new TypeResolver().resolve(BindExceptionResponse.class))
                .additionalModels(new TypeResolver().resolve(LinkResponse.class))
                .additionalModels(new TypeResolver().resolve(ErrorResponse.class))
                .directModelSubstitute(LinkResponse.class, Link.class);
    }

    private static <T> List<T> concatenatedList(List<T>... collections) {
        return Arrays.stream(collections).flatMap(Collection::stream).collect(Collectors.toList());
    }

    private List<ResponseMessage> messageUnprocessableEntity() {
        return Arrays.asList(new ResponseMessageBuilder()
                .code(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .message("Validation error")
                .responseModel(new ModelRef("BindExceptionResponse"))
                .build());
    }

    private List<ResponseMessage> messageNotFound() {
        return Arrays.asList(new ResponseMessageBuilder()
                .code(HttpStatus.NOT_FOUND.value())
                .message(HttpStatus.NOT_FOUND.getReasonPhrase())
                .responseModel(new ModelRef("ErrorResponse"))
                .build());
    }

    private List<ResponseMessage> messageCreated() {
        return Arrays.asList(new ResponseMessageBuilder()
                .code(HttpStatus.CREATED.value())
                .message(HttpStatus.CREATED.getReasonPhrase())
                .build());
    }

    private List<ResponseMessage> messageNoContent() {
        return Arrays.asList(new ResponseMessageBuilder()
                .code(HttpStatus.NO_CONTENT.value())
                .message(HttpStatus.NO_CONTENT.getReasonPhrase())
                .build());
    }

    @Bean
    public AlternateTypeRuleConvention pageableConvention(
            final TypeResolver resolver) {
        return new AlternateTypeRuleConvention() {

            @Override
            public int getOrder() {
                return Ordered.HIGHEST_PRECEDENCE;
            }

            @Override
            public List<AlternateTypeRule> rules() {
                return Arrays.asList(
                        newRule(resolver.resolve(Pageable.class), resolver.resolve(pageableMixin())),
                        newRule(resolver.resolve(Link.class), resolver.resolve(LinkResponse.class))
                );
            }
        };
    }

    private Type pageableMixin() {
        return new AlternateTypeBuilder()
                .fullyQualifiedClassName(
                        String.format("%s.generated.%s",
                                Pageable.class.getPackage().getName(),
                                Pageable.class.getSimpleName()))
                .withProperties(Arrays.asList(
                        property(Integer.class, "page"),
                        property(Integer.class, "size"),
                        property(String.class, "sort")
                ))
                .build();
    }

    private AlternateTypePropertyBuilder property(Class<?> type, String name) {
        return new AlternateTypePropertyBuilder()
                .withName(name)
                .withType(type)
                .withCanRead(true)
                .withCanWrite(true);
    }

    public interface LinkResponse {

        void setRel(String rel);

        void setHref(String rel);

        void setMethod(String rel);


    }

}
