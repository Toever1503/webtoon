package webtoon.domain.services;

import webtoon.domain.dtos.MangaGenreRelationDto;
import webtoon.domain.models.MangaGenreRelationModel;

public interface IMangaGenreRelationService {

	boolean deleteById(Long id);

	MangaGenreRelationDto update(MangaGenreRelationModel model);

	MangaGenreRelationDto add(MangaGenreRelationModel model);

}
