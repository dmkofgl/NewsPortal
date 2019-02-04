package dl.news.portal.web.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "dl.news.portal")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
