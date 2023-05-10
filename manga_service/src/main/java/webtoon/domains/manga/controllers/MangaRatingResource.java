package webtoon.domains.manga.controllers;

import org.springframework.web.bind.annotation.*;
import webtoon.account.entities.UserEntity;
import webtoon.domains.manga.dtos.ResponseDto;
import webtoon.domains.manga.models.MangaRatingModel;
import webtoon.domains.manga.services.IMangaRatingService;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/rating")
public class MangaRatingResource {
    private final IMangaRatingService ratingService;

    public MangaRatingResource(IMangaRatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping("/add")
    public ResponseDto add(@RequestBody MangaRatingModel model ,HttpSession session){
        return ResponseDto.of(this.ratingService.add(model,session));
    }

}
