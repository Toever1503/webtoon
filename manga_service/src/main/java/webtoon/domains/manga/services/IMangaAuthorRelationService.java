package webtoon.domains.manga.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import webtoon.domains.manga.dtos.MangaAuthorRelationDto;
import webtoon.domains.manga.entities.MangaAuthorRelationEntity;
import webtoon.domains.manga.models.MangaAuthorRelationModel;

public interface IMangaAuthorRelationService {

	boolean deleteById(Long id);

	MangaAuthorRelationDto update(MangaAuthorRelationModel model);

	MangaAuthorRelationDto add(MangaAuthorRelationModel model);

	Page<MangaAuthorRelationDto> filter(Pageable pageable, Specification<MangaAuthorRelationEntity> specs);

}
