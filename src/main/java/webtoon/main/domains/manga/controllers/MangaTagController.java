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
import webtoon.main.domains.manga.entities.MangaEntity;
import webtoon.main.domains.manga.entities.MangaEntity_;
import webtoon.main.domains.manga.entities.MangaGenreEntity;
import webtoon.main.domains.manga.entities.MangaGenreEntity_;
import webtoon.main.domains.manga.enums.EStatus;
import webtoon.main.domains.manga.services.IMangaGenreService;
import webtoon.main.domains.manga.services.IMangaService;
import webtoon.main.domains.tag.entity.TagEntity;
import webtoon.main.domains.tag.service.ITagService;

import java.util.List;

@Controller
@RequestMapping("tag")
public class MangaTagController {

    @Autowired
    @Lazy
    private ITagService tagService;

    @Autowired
    @Lazy
    private IMangaService mangaService;

    @GetMapping("{name}/{id}")
    public String listMangaForTag(@PathVariable Long id, @PathVariable String name, Model model, Pageable pageable){
        Specification mangaSpec = Specification.where(
                        (root, query, cb) -> cb.equal(root.get(MangaEntity_.STATUS), EStatus.DRAFTED).not()
                ).and((root, query, cb) -> cb.isNull(root.get(MangaEntity_.DELETED_AT)))
//                .and((root, query, criteriaBuilder)
//                        -> criteriaBuilder.equal(root.join(MangaEntity_.GENRES)
//                        .get(MangaGenreEntity_.ID),id))
                ;

        TagEntity tag = this.tagService.getById(id);
        List<TagEntity> tagEntityList =  this.tagService.findAll(Pageable.unpaged());


        Page<MangaEntity> mangaEntityPage = this.mangaService.filterEntities(pageable, mangaSpec);
        System.out.println("total page: "+ mangaEntityPage.getTotalPages());

        model.addAttribute("mangalist", mangaEntityPage.getContent());
        model.addAttribute("tagList", tagEntityList);
        model.addAttribute("tag", tag);

        model.addAttribute("hasPrevPage", mangaEntityPage.hasPrevious());
        model.addAttribute("hasNextPage", mangaEntityPage.hasNext());
        model.addAttribute("currentPage", mangaEntityPage.getNumber());
        model.addAttribute("totalPage", mangaEntityPage.getTotalPages());
        return "/manga/tag-page";
    }
}
