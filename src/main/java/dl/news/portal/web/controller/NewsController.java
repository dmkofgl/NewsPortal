package dl.news.portal.web.controller;

import dl.news.portal.domain.dto.NewsDto;
import dl.news.portal.domain.dto.NewsSearchingDto;
import dl.news.portal.domain.response.NewsResponse;
import dl.news.portal.domain.response.PageResponse;
import dl.news.portal.domain.service.NewsService;
import dl.news.portal.utils.ValidationMode;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

@RestController
@RequestMapping("/news")
public class NewsController {
    private static final String ID_PATH = "/{id}";

    @Autowired
    private NewsService newsService;

    @GetMapping
    @ApiImplicitParams({@ApiImplicitParam(name = "title", paramType = "query"),
            @ApiImplicitParam(name = "createDate", paramType = "query"),
            @ApiImplicitParam(name = "endCreateDate", paramType = "query")
    })
    public PageResponse<NewsResponse> getPageNews(NewsSearchingDto dto, Pageable pageable) {
        Page<NewsResponse> resourcePage = newsService.getFilteredPage(dto, pageable).map(NewsResponse::new);
        return new PageResponse<>(resourcePage);
    }

    @GetMapping(ID_PATH)
    public NewsResponse getNewsById(@PathVariable Long id) {
        return newsService.findNewsById(id)
                .map(NewsResponse::new)
                .orElseThrow(() -> new EntityNotFoundException("News with id " + id + "not found"));
    }

    @PostMapping
    public ResponseEntity<HttpStatus> addNews(@Validated(ValidationMode.Create.class) @RequestBody NewsDto newsDto) {
        newsService.createNews(newsDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping(ID_PATH)
    public ResponseEntity<HttpStatus> updateNews(@PathVariable Long id, @Valid @RequestBody NewsDto newsDto) {
        newsService.updateNews(id, newsDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(ID_PATH)
    public ResponseEntity<HttpStatus> deleteNews(@PathVariable Long id) {
        newsService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
