package webtoon.domains.manga.resources;

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
//        UserEntity user = (UserEntity) session.getAttribute("loggedUser");
//        if (user != null){
//            model.setCreatedBy(user.getId());
//        }
        model.setId(null);
        return ResponseDto.of(this.ratingService.add(model,session));
    }

    @PutMapping("/update/{id}")
    public ResponseDto update(@PathVariable Long id, @RequestBody MangaRatingModel model, HttpSession session){
        model.setId(id);
        return ResponseDto.of(this.ratingService.update(model,session));
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id){
        this.ratingService.deleteById(id);
    }


    @GetMapping("/getRating/{id}")
    public Double getAll(@PathVariable Long id){
        return this.ratingService.findRatingByMangaId(id);
    }


}
