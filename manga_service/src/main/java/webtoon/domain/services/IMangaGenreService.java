package webtoon.domain.services;

import webtoon.domain.dtos.MangaGenreDto;
import webtoon.domain.models.MangaGenreModel;

public interface IMangaGenreService {

	boolean deleteById(Long id);

	MangaGenreDto update(MangaGenreModel model);

	MangaGenreDto add(MangaGenreModel model);

}
