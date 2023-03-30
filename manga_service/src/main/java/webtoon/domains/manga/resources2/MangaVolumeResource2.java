package webtoon.domains.manga.resources2;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;
import webtoon.domains.manga.dtos.MangaVolumeDto;
import webtoon.domains.manga.entities.MangaEntity;
import webtoon.domains.manga.entities.MangaVolumeEntity;
import webtoon.domains.manga.models.MangaVolumeModel;
import webtoon.domains.manga.services.IMangaVolumeService;

import javax.persistence.criteria.Join;
import java.util.List;

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


    @GetMapping("get-all-for-manga/{id}")
    public List<MangaVolumeDto> getAllVolForManga(@PathVariable Long id) {
        Specification<MangaVolumeEntity> spec = ((root, query, cb) -> {
            Join<MangaEntity, MangaVolumeEntity> join = root.join("manga");
            return cb.equal(join.get("id"), id);
        });
        List<MangaVolumeDto> volumeDtoList = this.mangaVolumeService.filter(PageRequest.of(0, 9999), spec).toList();
//        if (volumeDtoList.size() == 0)
//            volumeDtoList = List.of(this.mangaVolumeService.add(MangaVolumeModel.builder()
//                    .mangaId(id)
//                    .name("Volume 1")
//                    .volumeIndex(0)
//                    .build()));
        return volumeDtoList;
    }
}
