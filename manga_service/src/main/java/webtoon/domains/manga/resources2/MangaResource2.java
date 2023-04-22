package webtoon.domains.manga.resources2;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;
import webtoon.domains.manga.dtos.MangaDto;
import webtoon.domains.manga.enums.EMangaDisplayType;
import webtoon.domains.manga.enums.EMangaSTS;
import webtoon.domains.manga.enums.EMangaType;
import webtoon.domains.manga.enums.EStatus;
import webtoon.domains.manga.models.MangaFilterInput;
import webtoon.domains.manga.models.MangaModel;
import webtoon.domains.manga.entities.MangaEntity_;
import webtoon.domains.manga.services.IMangaService;

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
//        MangaEntity_
        return this.mangaService.findById(id);
    }

    @PostMapping("filter")
    public Page<MangaDto> filter(@RequestBody MangaFilterInput input, Pageable page) {
        List<Specification> specs = new ArrayList<>();
        specs.add((root, query, cb) -> cb.equal(root.get(MangaEntity_.STATUS), EStatus.DRAFTED).not());

        if (input.getStatus() != null)
            if (!input.getStatus().equals(EStatus.ALL))
                specs.add((root, query, cb) -> cb.equal(root.get(MangaEntity_.STATUS), input.getStatus()));

        if (input.getQ() != null) {
            String qRegex = "%" + input.getQ() + "%";
            specs.add((root, query, cb) -> cb.like(root.get(MangaEntity_.TITLE), qRegex));
        }
        Specification finalSpec = null;
        for (Specification s : specs) {
            if (finalSpec == null)
                finalSpec = Specification.where(s);
            else finalSpec.and(s);
        }
        return this.mangaService.filter(page, finalSpec);
    }

    @PostMapping
    public MangaDto saveMangaInfo(@RequestBody MangaModel input) {
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
