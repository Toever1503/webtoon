package webtoon.domains.manga.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import webtoon.domains.manga.dtos.MangaGenreDto;
import webtoon.domains.manga.entities.MangaGenreEntity;
import webtoon.domains.manga.models.MangaGenreModel;

import java.util.List;

public interface IMangaGenreService {

	boolean deleteById(Long id);

	MangaGenreDto update(MangaGenreModel model);

	MangaGenreDto add(MangaGenreModel model);

	Page<MangaGenreDto> filter(Pageable pageable, Specification<MangaGenreEntity> specs);

    Page<MangaGenreEntity> filterGenre(Specification spec, Pageable page);

	void deleteGenreByIds(List<Long> ids);
}
