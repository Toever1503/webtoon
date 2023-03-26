package webtoon.domains.manga.resources2;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import webtoon.domains.manga.dtos.MangaDto;
import webtoon.domains.manga.models.MangaModel;
import webtoon.domains.manga.services.IMangaService;

@RestController
@RequestMapping("api/manga")
public class MangaResource2 {

    private final IMangaService mangaService;

    public MangaResource2(IMangaService mangaService) {
        this.mangaService = mangaService;
    }

    @PostMapping
    public MangaDto createMangaInfo(@RequestBody MangaModel input) {
        return mangaService.add(input);
    }
}
