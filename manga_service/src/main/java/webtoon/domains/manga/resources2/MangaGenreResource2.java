package webtoon.domains.manga.resources2;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;
import webtoon.domains.manga.dtos.MangaGenreDto;
import webtoon.domains.manga.entities.MangaGenreEntity;
import webtoon.domains.manga.entities.MangaGenreEntity_;
import webtoon.domains.manga.models.MangaGenreModel;
import webtoon.domains.manga.services.IMangaGenreService;
import webtoon.domains.tag.entity.TagEntity;

import java.util.List;

@RestController
@RequestMapping("api/genre")
public class MangaGenreResource2 {
    private final IMangaGenreService mangaGenreService;

    public MangaGenreResource2(IMangaGenreService mangaGenreService) {
        this.mangaGenreService = mangaGenreService;
    }

    @GetMapping("filter")
    public Page<MangaGenreEntity> filter(@RequestParam String s, Pageable page) {
        Specification<MangaGenreEntity> spec = (root, query, cb) ->
                cb.like(root.get(MangaGenreEntity_.NAME), "%" + s + "%");
        return this.mangaGenreService.filterGenre(spec, page);
    }

    @PostMapping
    public MangaGenreEntity addNew(@RequestBody MangaGenreModel input) {
        input.setId(null);
        return this.mangaGenreService.add(input);
    }

    @PutMapping("{id}")
    public MangaGenreEntity update(@PathVariable Long id, @RequestBody MangaGenreModel input) {
        input.setId(id);
        return this.mangaGenreService.update(input);
    }

    @DeleteMapping("bulk-del")
    public void deleteBulk(@RequestParam List<Long> ids) {
        this.mangaGenreService.deleteGenreByIds(ids);
    }
}
