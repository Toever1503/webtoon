package webtoon.main.domains.manga.controllers;

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
import webtoon.main.domains.manga.entities.*;
import webtoon.main.domains.manga.enums.EStatus;
import webtoon.main.domains.manga.services.IMangaGenreService;
import webtoon.main.domains.manga.services.IMangaService;

import java.util.List;

@Controller
@RequestMapping("genre")
public class MangaGenreController {

    @Autowired
    @Lazy
    private IMangaGenreService genreService;

    @Autowired
    @Lazy
    private IMangaService mangaService;

    @GetMapping("{name}/{id}")
    public String mangaGenre(@PathVariable Long id, @PathVariable String name, Model model, Pageable pageable){
        Specification mangaSpec = Specification.where(
                (root, query, cb) -> cb.equal(root.get(MangaEntity_.STATUS), EStatus.DRAFTED).not()
        ).and((root, query, cb) -> cb.isNull(root.get(MangaEntity_.DELETED_AT)))
                .and((root, query, criteriaBuilder)
                        -> criteriaBuilder.equal(root.join(MangaEntity_.GENRES)
                .get(MangaGenreEntity_.ID),id)
                );

        MangaGenreEntity genre = this.genreService.getById(id);
        List<MangaGenreEntity> mangaGenre =  this.genreService.findAllGenre();

        Page<MangaEntity> mangaEntityPage = this.mangaService.filterEntities(pageable, mangaSpec);

        System.out.println("total page: "+ mangaEntityPage.getTotalPages());

        model.addAttribute("mangalist", mangaEntityPage.getContent());
        model.addAttribute("genreList", mangaGenre);
        model.addAttribute("mangaGenre", genre);

        model.addAttribute("hasPrevPage", mangaEntityPage.hasPrevious());
        model.addAttribute("hasNextPage", mangaEntityPage.hasNext());
        model.addAttribute("currentPage", mangaEntityPage.getNumber());
        model.addAttribute("totalPage", mangaEntityPage.getTotalPages());
        return "/manga/genrePage";
    }
}
