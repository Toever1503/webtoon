package webtoon.domains.manga.resources2;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import webtoon.domains.manga.dtos.MangaVolumeDto;
import webtoon.domains.manga.models.MangaVolumeFilterInput;
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
    public MangaVolumeDto createVolume(@RequestBody MangaVolumeModel input) {
        return this.mangaVolumeService.add(input);
    }
    @PostMapping("{id}")
    public MangaVolumeDto updateVolume(@PathVariable Long id, @RequestBody MangaVolumeModel input) {
        input.setId(id);
        return this.mangaVolumeService.update(input);
    }


    @PostMapping("filter")
    public Page<MangaVolumeDto> getAllVolForManga(@RequestBody MangaVolumeFilterInput input, Pageable pageable) {
        Page<MangaVolumeDto> volumeDtoList = this.mangaVolumeService.filterVolume(pageable, input);
        return volumeDtoList;
    }

    @GetMapping("get-last-vol-index/{id}")
    public MangaVolumeDto getLastVolIndex(@PathVariable Long id) {
        return this.mangaVolumeService.getLastVolIndex(id);
    }
    @DeleteMapping("{id}")
    public void deleteById(@PathVariable Long id){
        this.mangaVolumeService.deleteById(id);
    }
}
