package dl.news.portal.web.controller;

import dl.news.portal.domain.dto.NewsDto;
import dl.news.portal.domain.entity.News;
import dl.news.portal.domain.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
@RestController
@RequestMapping("/news")
public class NewsController {
    @Autowired
    private NewsService newsService;

    @GetMapping
    public ResponseEntity<Page<News>> getPageNews(Pageable pageable) {
        return new ResponseEntity<>(newsService.getNewsPage(pageable), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<News> getNewsById(@PathVariable Long id) {
        Optional<News> optionalNews = newsService.findNewsById(id);
        return optionalNews.map(news -> new ResponseEntity<>(news, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<HttpStatus> addNews(News news) {
        HttpStatus httpStatus = HttpStatus.OK;
        try {
            newsService.createNews(news);
        } catch (Exception e) {
            httpStatus = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(httpStatus);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> updateUser(@PathVariable Long id, NewsDto newsDto) {
        HttpStatus httpStatus = HttpStatus.OK;
        try {
            newsService.updateNews(id, newsDto);
        } catch (Exception e) {
            httpStatus = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(httpStatus);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable Long id) {
        HttpStatus httpStatus = HttpStatus.OK;
        try {
            newsService.deleteById(id);
        } catch (Exception e) {
            httpStatus = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(httpStatus);
    }

}
