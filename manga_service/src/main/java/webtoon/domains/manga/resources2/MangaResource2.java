package webtoon.domains.manga.resources2;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import webtoon.domains.manga.dtos.MangaDto;
import webtoon.domains.manga.enums.EMangaType;
import webtoon.domains.manga.models.MangaModel;
import webtoon.domains.manga.services.IMangaService;

@RestController
@RequestMapping("api/manga")
public class MangaResource2 {

    private final IMangaService mangaService;

    public MangaResource2(IMangaService mangaService) {
        this.mangaService = mangaService;
    }

//    @PostMapping("filter")
//    public Page<MangaDto> filterManga()
    @PostMapping
    public MangaDto createMangaInfo(@RequestBody MangaModel input) {
        return mangaService.add(input);
    }

    @PatchMapping("set-type/{id}")
    public void setMangaType(@PathVariable Long id, @RequestParam EMangaType type){
        if(type.equals(EMangaType.UNSET))
            throw new RuntimeException("type is not support");
        this.mangaService.setMangaType(id, type);
    }


}
