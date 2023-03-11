package webtoon.domain.services;

import webtoon.domain.dtos.MangaAuthorRelationDto;
import webtoon.domain.models.MangaAuthorRelationModel;

public interface IMangaAuthorRelationService {

	boolean deleteById(Long id);

	MangaAuthorRelationDto update(MangaAuthorRelationModel model);

	MangaAuthorRelationDto add(MangaAuthorRelationModel model);

}
