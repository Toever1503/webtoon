package webtoon.domains.manga.resources2;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import webtoon.domains.manga.dtos.MangaVolumeDto;
import webtoon.domains.manga.models.MangaVolumeModel;
import webtoon.domains.manga.services.IMangaVolumeService;

@RestController
@RequestMapping("api/manga/volume")
public class MangaVolumeResource2 {

    private final IMangaVolumeService mangaVolumeService;

    public MangaVolumeResource2(IMangaVolumeService mangaVolumeService) {
        this.mangaVolumeService = mangaVolumeService;
    }

    @PostMapping
    public MangaVolumeDto createVolume(@RequestBody MangaVolumeModel input){
        return this.mangaVolumeService.add(input);
    }
}
