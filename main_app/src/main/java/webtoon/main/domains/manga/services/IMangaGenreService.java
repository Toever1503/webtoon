package webtoon.main.domains.manga.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import webtoon.main.domains.manga.dtos.MangaGenreDto;
import webtoon.main.domains.manga.entities.MangaGenreEntity;
import webtoon.main.domains.manga.models.MangaGenreModel;

import java.util.List;

public interface IMangaGenreService {

	boolean deleteById(Long id);

	MangaGenreEntity update(MangaGenreModel model);

	MangaGenreEntity add(MangaGenreModel model);

	Page<MangaGenreDto> filter(Pageable pageable, Specification<MangaGenreEntity> specs);

    Page<MangaGenreEntity> filterGenre(Specification spec, Pageable page);

	void deleteGenreByIds(List<Long> ids);

	MangaGenreEntity getById(Long id);

	List<MangaGenreEntity> findAllGenre();

    List<MangaGenreEntity> getAll();
}
