package webtoon.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import webtoon.dtos.MangaDto;
import webtoon.entities.MangaEntity;
import webtoon.models.MangaModel;

public interface IMangaService {

	MangaDto add(MangaModel model);

	Page<MangaDto> filter(Pageable pageable, Specification<MangaEntity> specs);

	boolean deleteById(Long id);

	MangaDto update(MangaModel model);


}
