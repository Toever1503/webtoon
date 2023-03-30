package webtoon.domains.manga.resources2;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;
import webtoon.domains.manga.dtos.MangaDto;
import webtoon.domains.manga.enums.EMangaType;
import webtoon.domains.manga.enums.EStatus;
import webtoon.domains.manga.models.MangaFilterInput;
import webtoon.domains.manga.models.MangaModel;
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


    @PostMapping("filter")
    public Page<MangaDto> filter(@RequestBody MangaFilterInput input, Pageable page) {
        List<Specification> specs = new ArrayList<>();
        if (input.getStatus() != null)
            if (!input.getStatus().equals(EStatus.ALL))
                specs.add((root, query, cb) -> cb.equal(root.get("status"), input.getStatus()));

        if (input.getQ() != null) {
            String qRegex = "%" + input.getQ() + "%";
            specs.add((root, query, cb) -> cb.like(root.get("title"), qRegex));
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
    public MangaDto createMangaInfo(@RequestBody MangaModel input) {
        return mangaService.add(input);
    }

    @PatchMapping("set-type/{id}")
    public void setMangaType(@PathVariable Long id, @RequestParam EMangaType type) {
        if (type.equals(EMangaType.UNSET))
            throw new RuntimeException("type is not support");
        this.mangaService.setMangaType(id, type);
    }


}
