package dl.news.portal.web.controller;

import dl.news.portal.domain.dto.NewsDto;
import dl.news.portal.domain.entity.News;
import dl.news.portal.domain.resource.NewsResource;
import dl.news.portal.domain.resource.PageResource;
import dl.news.portal.domain.service.NewsService;
import dl.news.portal.exception.EntityNotExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/news")
public class NewsController {
    @Autowired
    private NewsService newsService;

    @GetMapping
    @ApiImplicitParams({@ApiImplicitParam(name = "title", paramType = "query"),
            @ApiImplicitParam(name = "createDate", paramType = "query"),
            @ApiImplicitParam(name = "endCreateDate", paramType = "query")
    })
    public PageResource<NewsResource> getPageNews(NewsSearchingDto dto, Pageable pageable) {
        Page<NewsResource> resourcePage = newsService.getFilteredPage(dto, pageable).map(NewsResource::new);
        return new PageResource<>(resourcePage);
    }

    @GetMapping("/{id}")
    public NewsResource getNewsById(@PathVariable Long id) {
        return newsService.findNewsById(id)
                .map(NewsResource::new)
                .orElseThrow(() -> new EntityNotExistsException(News.class.getSimpleName()));
    }

    @PostMapping
    public ResponseEntity<HttpStatus> addNews(@Valid @RequestBody NewsDto news) {
        newsService.createNews(news.transform());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> updateNews(@PathVariable Long id, @Valid @RequestBody NewsDto newsDto) {
        newsService.updateNews(id, newsDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteNews(@PathVariable Long id) {
        newsService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
