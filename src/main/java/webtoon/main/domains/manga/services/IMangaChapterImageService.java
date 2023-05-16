package webtoon.main.domains.manga.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import webtoon.main.domains.manga.dtos.MangaChapterImageDto;
import webtoon.main.domains.manga.entities.MangaChapterImageEntity;
import webtoon.main.domains.manga.models.MangaChapterImageModel;

public interface IMangaChapterImageService {

	boolean deleteById(Long id);

	MangaChapterImageDto update(MangaChapterImageModel model);

	MangaChapterImageDto add(MangaChapterImageModel model);

	Page<MangaChapterImageDto> filter(Pageable pageable, Specification<MangaChapterImageEntity> specs);

}
