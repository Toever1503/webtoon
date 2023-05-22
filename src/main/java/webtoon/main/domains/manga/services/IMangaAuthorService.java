package webtoon.main.domains.manga.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import webtoon.main.domains.manga.dtos.MangaAuthorDto;
import webtoon.main.domains.manga.models.MangaAuthorModel;
import webtoon.main.domains.manga.entities.MangaAuthorEntity;

import java.util.List;

public interface IMangaAuthorService {

	boolean deleteById(Long id);

	MangaAuthorEntity getById(Long id);

	MangaAuthorEntity update(MangaAuthorModel model);

	MangaAuthorEntity add(MangaAuthorModel model);

	Page<MangaAuthorDto> filter(Pageable pageable, Specification<MangaAuthorEntity> specs);

	Page<MangaAuthorEntity> filterAuthor(Specification spec, Pageable page);

	void deleteAuthorByIds(List<Long> ids);

    List<MangaAuthorEntity> getAll();


	void refreshAuthorCount(List<Long> authorIds);
}
