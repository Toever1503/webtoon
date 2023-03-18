package webtoon.domains.manga.services;

import webtoon.domains.manga.dtos.MangaGenreDto;
import webtoon.domains.manga.models.MangaGenreModel;

public interface IMangaGenreService {

	boolean deleteById(Long id);

	MangaGenreDto update(MangaGenreModel model);

	MangaGenreDto add(MangaGenreModel model);

}
