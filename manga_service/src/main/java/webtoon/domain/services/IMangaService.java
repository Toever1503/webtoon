package webtoon.domain.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import webtoon.domain.dtos.MangaDto;
import webtoon.domain.entities.MangaEntity;
import webtoon.domain.models.MangaModel;

public interface IMangaService {

	MangaDto add(MangaModel model);

	Page<MangaDto> filter(Pageable pageable, Specification<MangaEntity> specs);

	boolean deleteById(Long id);

	MangaDto update(MangaModel model);


}
