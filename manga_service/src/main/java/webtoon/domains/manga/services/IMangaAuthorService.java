package webtoon.domains.manga.services;

import webtoon.domains.manga.dtos.MangaAuthorDto;
import webtoon.domains.manga.entities.MangaAuthorEntity;
import webtoon.domains.manga.models.MangaAuthorModel;

public interface IMangaAuthorService {

	boolean deleteById(Long id);

	MangaAuthorEntity getById(Long id);

	MangaAuthorDto update(MangaAuthorModel model);

	MangaAuthorDto add(MangaAuthorModel model);

}
