package webtoon.domains.manga.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import webtoon.domains.manga.entities.MangaEntity;
import webtoon.domains.manga.entities.MangaEntity_;
import webtoon.domains.manga.entities.MangaGenreEntity;
import webtoon.domains.manga.entities.MangaGenreEntity_;
import webtoon.domains.manga.enums.EStatus;
import webtoon.domains.manga.services.IMangaGenreService;
import webtoon.domains.manga.services.IMangaService;

import java.util.List;

@Controller
@RequestMapping("manga-gener")
public class MangaGenreController {

    @Autowired
    @Lazy
    private IMangaGenreService genreService;

    @Autowired
    @Lazy
    private IMangaService mangaService;

    @GetMapping("{name}/{id}")
    public String mangaGener(@PathVariable Long id, @PathVariable String name, Model model, Pageable pageable){
        Specification mangaSpec = Specification.where(
                (root, query, cb) -> cb.equal(root.get(MangaEntity_.STATUS), EStatus.DRAFTED).not()
        ).and((root, query, cb) -> cb.isNull(root.get(MangaEntity_.DELETED_AT)))
                .and((root, query, criteriaBuilder)
                        -> criteriaBuilder.equal(root.join(MangaEntity_.GENRES)
                .get(MangaGenreEntity_.ID),id)
                );

        Page<MangaEntity> mangaEntity = this.mangaService.filterEntities(pageable, mangaSpec);

        MangaGenreEntity genre = this.genreService.getById(id);
        List<MangaGenreEntity> mangaGenre =  this.genreService.findAllGenre();
        model.addAttribute("mangaGenre",mangaGenre);
        model.addAttribute("mangaEntity", mangaEntity);
        model.addAttribute("generId",genre);
        return "/manga/genre";
    }
}
