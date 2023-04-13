package webtoon.domains.manga.resources;

import org.springframework.web.bind.annotation.*;
import webtoon.domains.manga.dtos.ResponseDto;
import webtoon.domains.manga.models.MangaRatingModel;
import webtoon.domains.manga.services.IMangaRatingService;

@RestController
@RequestMapping(name = "/rating")

public class MangaRatingResource {
    private final IMangaRatingService ratingService;

    public MangaRatingResource(IMangaRatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping("/add")
    public ResponseDto add(@RequestBody MangaRatingModel model){
        model.setId(null);
        return ResponseDto.of(this.ratingService.add(model));
    }

    @PutMapping("/update/{id}")
    public ResponseDto update(@PathVariable Long id, @RequestBody MangaRatingModel model){
        model.setId(id);
        return ResponseDto.of(this.ratingService.update(model));
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id){
        this.ratingService.deleteById(id);
    }
}
