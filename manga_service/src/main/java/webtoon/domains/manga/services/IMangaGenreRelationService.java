package webtoon.domains.manga.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import webtoon.domains.manga.dtos.MangaGenreRelationDto;
import webtoon.domains.manga.entities.MangaGenreRelationEntity;
import webtoon.domains.manga.models.MangaGenreRelationModel;

public interface IMangaGenreRelationService {

	boolean deleteById(Long id);

	MangaGenreRelationDto update(MangaGenreRelationModel model);

	MangaGenreRelationDto add(MangaGenreRelationModel model);

	Page<MangaGenreRelationDto> filter(Pageable pageable, Specification<MangaGenreRelationEntity> specs);

}
