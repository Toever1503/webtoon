package webtoon.domains.manga.services;

import webtoon.domains.manga.dtos.MangaChapterImageDto;
import webtoon.domains.manga.models.MangaChapterImageModel;

public interface IMangaChapterImageService {

	boolean deleteById(Long id);

	MangaChapterImageDto update(MangaChapterImageModel model);

	MangaChapterImageDto add(MangaChapterImageModel model);

}
