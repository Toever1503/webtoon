package webtoon.main.domains.manga.services;

import org.springframework.data.domain.Page;
import webtoon.main.domains.manga.dtos.MangaDto;
import webtoon.main.domains.manga.dtos.MangaRatingDto;
import webtoon.main.domains.manga.entities.MangaRatingEntity;
import webtoon.main.domains.manga.models.MangaRatingModel;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IMangaRatingService {


    MangaRatingDto add(MangaRatingModel model, HttpSession session);

    Page<MangaDto> findAllById(Long id);

    List<Map> getRating(Long id);

    Double findRatingByMangaId(Long id);
}
