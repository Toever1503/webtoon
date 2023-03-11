package webtoon.domain.services;

import webtoon.domain.dtos.MangaChapterImageDto;
import webtoon.domain.models.MangaChapterImageModel;

public interface IMangaChapterImageService {

	boolean deleteById(Long id);

	MangaChapterImageDto update(MangaChapterImageModel model);

	MangaChapterImageDto add(MangaChapterImageModel model);

}
