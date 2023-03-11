package webtoon.domain.services;

import webtoon.domain.dtos.MangaAuthorDto;
import webtoon.domain.entities.MangaAuthorEntity;
import webtoon.domain.models.MangaAuthorModel;

public interface IMangaAuthorService {

	boolean deleteById(Long id);

	MangaAuthorEntity getById(Long id);

	MangaAuthorDto update(MangaAuthorModel model);

	MangaAuthorDto add(MangaAuthorModel model);

}
