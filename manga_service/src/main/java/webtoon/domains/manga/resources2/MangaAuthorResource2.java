package webtoon.domains.manga.resources2;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;
import webtoon.domains.manga.dtos.MangaAuthorDto;
import webtoon.domains.manga.dtos.MangaGenreDto;
import webtoon.domains.manga.entities.MangaAuthorEntity;
import webtoon.domains.manga.entities.MangaGenreEntity;
import webtoon.domains.manga.models.MangaAuthorModel;
import webtoon.domains.manga.models.MangaGenreModel;
import webtoon.domains.manga.services.IMangaAuthorService;
import webtoon.domains.manga.services.IMangaGenreService;

import java.util.List;

@RestController
@RequestMapping("api/author")
public class MangaAuthorResource2 {
    private final IMangaAuthorService mangaAuthorService;

    public MangaAuthorResource2(IMangaAuthorService mangaAuthorService) {
        this.mangaAuthorService = mangaAuthorService;
    }

    @GetMapping("filter")
    public Page<MangaAuthorEntity> filter(@RequestParam String s, Pageable page) {
        Specification<MangaAuthorEntity> spec = (root, query, cb) ->
                cb.like(root.get("name"), "%" + s + "%");
        return this.mangaAuthorService.filterAuthor(spec, page);
    }

    @PostMapping
    public MangaAuthorEntity addNew(@RequestBody MangaAuthorModel input) {
        input.setId(null);
        return this.mangaAuthorService.add(input);
    }

    @PutMapping("{id}")
    public MangaAuthorEntity update(@PathVariable Long id, @RequestBody MangaAuthorModel input) {
        input.setId(id);
        return this.mangaAuthorService.update(input);
    }

    @DeleteMapping("bulk-del")
    public void deleteBulk(@RequestParam List<Long> ids) {
        this.mangaAuthorService.deleteAuthorByIds(ids);
    }
}
