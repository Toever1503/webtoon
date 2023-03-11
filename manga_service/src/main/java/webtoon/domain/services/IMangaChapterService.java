package webtoon.domain.services;

import webtoon.domain.dtos.MangaChapterDto;
import webtoon.domain.models.MangaChapterModel;

public interface IMangaChapterService {

	boolean deleteById(Long id);

	MangaChapterDto update(MangaChapterModel model);

	MangaChapterDto add(MangaChapterModel model);

}
