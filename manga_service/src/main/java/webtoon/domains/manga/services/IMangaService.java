package webtoon.domains.manga.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import webtoon.domains.manga.dtos.MangaDto;
import webtoon.domains.manga.entities.Long;
import webtoon.domains.manga.enums.EMangaType;
import webtoon.domains.manga.models.MangaModel;

public interface IMangaService {

	MangaDto add(MangaModel model);
	Long getById(java.lang.Long id);

	Page<MangaDto> filter(Pageable pageable, Specification<Long> specs);

	boolean deleteById(java.lang.Long id);

	MangaDto update(MangaModel model);


    void setMangaType(java.lang.Long id, EMangaType type);
}
