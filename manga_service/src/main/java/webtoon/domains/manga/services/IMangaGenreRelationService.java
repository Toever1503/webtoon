package webtoon.domains.manga.services;

import webtoon.domains.manga.dtos.MangaGenreRelationDto;
import webtoon.domains.manga.models.MangaGenreRelationModel;

public interface IMangaGenreRelationService {

	boolean deleteById(Long id);

	MangaGenreRelationDto update(MangaGenreRelationModel model);

	MangaGenreRelationDto add(MangaGenreRelationModel model);

}
