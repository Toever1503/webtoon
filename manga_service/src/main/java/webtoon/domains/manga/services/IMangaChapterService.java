package webtoon.domains.manga.services;

import webtoon.domains.manga.dtos.MangaChapterDto;
import webtoon.domains.manga.models.MangaChapterModel;

public interface IMangaChapterService {

	boolean deleteById(Long id);

	MangaChapterDto update(MangaChapterModel model);

	MangaChapterDto add(MangaChapterModel model);

}
