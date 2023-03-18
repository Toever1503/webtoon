package webtoon.domains.manga.services;

import webtoon.domains.manga.dtos.MangaAuthorRelationDto;
import webtoon.domains.manga.models.MangaAuthorRelationModel;

public interface IMangaAuthorRelationService {

	boolean deleteById(Long id);

	MangaAuthorRelationDto update(MangaAuthorRelationModel model);

	MangaAuthorRelationDto add(MangaAuthorRelationModel model);

}
