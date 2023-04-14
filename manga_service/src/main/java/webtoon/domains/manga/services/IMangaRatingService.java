package webtoon.domains.manga.services;

import org.springframework.data.domain.Page;
import webtoon.domains.manga.dtos.MangaDto;
import webtoon.domains.manga.dtos.MangaRatingDto;
import webtoon.domains.manga.entities.MangaRatingEntity;
import webtoon.domains.manga.models.MangaRatingModel;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IMangaRatingService {
    MangaRatingDto add(MangaRatingModel model);

    MangaRatingDto update(MangaRatingModel model);

    MangaRatingEntity getById(Long id);

    boolean deleteById(Long id);

    Page<MangaDto> findAllById(Long id);


    List<Map> getRating(Long id);
}
