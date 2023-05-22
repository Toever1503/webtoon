package webtoon.main.domains.manga.resources2;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;
import webtoon.main.domains.manga.dtos.MangaDto;
import webtoon.main.domains.manga.entities.MangaAuthorEntity_;
import webtoon.main.domains.manga.entities.MangaEntity;
import webtoon.main.domains.manga.entities.MangaGenreEntity_;
import webtoon.main.domains.manga.enums.EMangaDisplayType;
import webtoon.main.domains.manga.enums.EMangaSTS;
import webtoon.main.domains.manga.enums.EMangaType;
import webtoon.main.domains.manga.enums.EStatus;
import webtoon.main.domains.manga.models.MangaFilterInput;
import webtoon.main.domains.manga.models.MangaModel;
import webtoon.main.domains.manga.entities.MangaEntity_;
import webtoon.main.domains.manga.services.IMangaService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/manga")
public class MangaResource2 {

    private final IMangaService mangaService;

    public MangaResource2(IMangaService mangaService) {
        this.mangaService = mangaService;
    }

    @GetMapping("{id}")
    public MangaDto findById(@PathVariable Long id) {
        return this.mangaService.findById(id);
    }

    @PostMapping("filter")
    public Page<MangaDto> filter(@RequestBody MangaFilterInput input, Pageable page) {
        List<Specification<MangaEntity>> listSpecs = new ArrayList<>();

        if (input.getStatus() != null) {
            listSpecs.add((root, query, cb) -> cb.equal(root.get(MangaEntity_.MANGA_STATUS), input.getStatus()));
        }

        if (input.getQ() != null) {
            String qRegex = "%" + input.getQ() + "%";
            listSpecs.add((root, query, cb) -> cb.or(
                    cb.like(root.get(MangaEntity_.TITLE), qRegex),
                    cb.like(root.get(MangaEntity_.mangaName), qRegex)
            ));
        }

        if (input.getGenreId() != null) {
            listSpecs.add((root, query, cb) -> root.join(MangaEntity_.GENRES).get(MangaGenreEntity_.ID).in(input.getGenreId()));
        }
        if (input.getAuthorId() != null) {
            listSpecs.add((root, query, cb) -> root.join(MangaEntity_.AUTHORS).get(MangaAuthorEntity_.ID).in(input.getAuthorId()));
        }
        if (input.getIsShow() != null) {
            listSpecs.add((root, query, cb) -> cb.equal(root.get(MangaEntity_.IS_SHOW), input.getIsShow()));
        }
        if (input.getMangaType() != null) {
            listSpecs.add(((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(MangaEntity_.MANGA_TYPE), input.getMangaType())));
        }

        listSpecs.add((root, query, cb) -> cb.equal(root.get(MangaEntity_.STATUS), EStatus.DRAFTED).not());
        listSpecs.add((root, query, cb) -> cb.isNull(root.get(MangaEntity_.DELETED_AT)));

        Specification<MangaEntity> finalSpec = null;
        for (Specification spec : listSpecs) {
            if (finalSpec == null) {
                finalSpec = Specification.where(spec);
            } else {
                finalSpec = finalSpec.and(spec);
            }
        }

        return this.mangaService.filter(page, finalSpec);
//        return this.mangaService.filter(page, Specification.where((root, query, cb) -> cb.equal(root.get(MangaEntity_.IS_SHOW), input.getIsShow())));
    }

    @PostMapping
    public MangaDto saveMangaInfo(MangaModel input) {
        return mangaService.add(input);
    }

    @PatchMapping("set-manga-type-and-display-type/{id}")
    public void setMangaTypeAndDisplayType(@PathVariable Long id, @RequestParam EMangaType mangaType, @RequestParam EMangaDisplayType displayType) {
        if (mangaType.equals(EMangaType.UNSET))
            throw new RuntimeException("type is not support");

        this.mangaService.setMangaTypeAndDisplayType(id, mangaType, displayType);
    }

    @PatchMapping("reset-manga-type/{id}")
    public void resetMangaType(@PathVariable Long id) {
        this.mangaService.resetMangaType(id);
    }

    @PatchMapping("change-status/{id}")
    public void changeStatus(@PathVariable Long id, @RequestParam EStatus status) {
        this.mangaService.changeStatus(id, status);
    }

    @PatchMapping("change-release-status/{id}")
    public void changeReleaseStatus(@PathVariable Long id, @RequestParam EMangaSTS mangaSTS) {
        this.mangaService.changeReleaseStatus(id, mangaSTS);
    }

    @DeleteMapping("{id}")
    public void deleteById(@PathVariable Long id) {
        this.mangaService.deleteById(id);
    }

    @GetMapping("calc-total-manga-each-status")
    public List<Object[]> calculateTotalMangaEachStatus(@RequestParam(required = false) String q) {
        return this.mangaService.calculateTotalMangaEachStatus(q);
    }
}
