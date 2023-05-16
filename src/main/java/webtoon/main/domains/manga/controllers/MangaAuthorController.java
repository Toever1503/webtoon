package webtoon.main.domains.manga.controllers;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import webtoon.main.domains.manga.entities.MangaAuthorEntity;
import webtoon.main.domains.manga.entities.MangaAuthorEntity_;
import webtoon.main.domains.manga.entities.MangaEntity;
import webtoon.main.domains.manga.entities.MangaEntity_;
import webtoon.main.domains.manga.enums.EStatus;
import webtoon.main.domains.manga.services.IMangaAuthorService;
import webtoon.main.domains.manga.services.IMangaService;

@Controller
@RequestMapping("author")
public class MangaAuthorController {

    private final IMangaAuthorService mangaAuthorService;
    private final IMangaService mangaService;

    public MangaAuthorController(IMangaAuthorService mangaAuthorService, IMangaService mangaService) {
        this.mangaAuthorService = mangaAuthorService;
        this.mangaService = mangaService;
    }

    @GetMapping("{name}/{id}")
    public String listMangaOfAuthor(@PathVariable Long id, Pageable pageable, Model model) {
        MangaAuthorEntity authorityEntity = this.mangaAuthorService.getById(id);
        model.addAttribute("author", authorityEntity);
        model.addAttribute("authorList", this.mangaAuthorService.filterAuthor(null, PageRequest.of(0, 9999)).getContent());


        Specification mangaSpec = Specification
                .where((root, query, cb) -> cb.equal(root.get(MangaEntity_.STATUS), EStatus.DRAFTED).not())
                .and((root, query, cb) -> cb.isNull(root.get(MangaEntity_.DELETED_AT)))
                .and(((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.join(MangaEntity_.AUTHORS).get(MangaAuthorEntity_.ID), id)));

        Page<MangaEntity> mangaEntityPage = this.mangaService.filterEntities(pageable, mangaSpec);

        System.out.println("total page: "+ mangaEntityPage.getTotalPages());

        model.addAttribute("mangalist", mangaEntityPage);

        model.addAttribute("hasPrevPage", mangaEntityPage.hasPrevious());
        model.addAttribute("hasNextPage", mangaEntityPage.hasNext());
        model.addAttribute("currentPage", mangaEntityPage.getNumber());
        model.addAttribute("totalPage", mangaEntityPage.getTotalPages());
        return "manga/authorPage";
    }
}
