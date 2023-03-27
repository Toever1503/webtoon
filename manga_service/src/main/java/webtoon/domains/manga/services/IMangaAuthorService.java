package webtoon.domains.manga.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import webtoon.domains.manga.dtos.MangaAuthorDto;
import webtoon.domains.manga.entities.MangaAuthorEntity;
import webtoon.domains.manga.entities.MangaGenreEntity;
import webtoon.domains.manga.models.MangaAuthorModel;

import java.util.List;

public interface IMangaAuthorService {

	boolean deleteById(Long id);

	MangaAuthorEntity getById(Long id);

	MangaAuthorDto update(MangaAuthorModel model);

	MangaAuthorDto add(MangaAuthorModel model);

	Page<MangaAuthorDto> filter(Pageable pageable, Specification<MangaAuthorEntity> specs);

	Page<MangaAuthorEntity> filterAuthor(Specification spec, Pageable page);

	void deleteAuthorByIds(List<Long> ids);
}
