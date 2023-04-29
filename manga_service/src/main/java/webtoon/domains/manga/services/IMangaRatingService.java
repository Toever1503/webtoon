package webtoon.domains.manga.services;

import org.springframework.data.domain.Page;
import webtoon.domains.manga.dtos.MangaDto;
import webtoon.domains.manga.dtos.MangaRatingDto;
import webtoon.domains.manga.entities.MangaRatingEntity;
import webtoon.domains.manga.models.MangaRatingModel;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IMangaRatingService {


    MangaRatingDto add(MangaRatingModel model, HttpSession session);

    MangaRatingDto update(MangaRatingModel model, HttpSession session);

    MangaRatingEntity getById(Long id, Long createId);

    boolean deleteById(Long id);

    Page<MangaDto> findAllById(Long id);


    List<Map> getRating(Long id);

    Double findRatingByMangaId(Long id);
}
