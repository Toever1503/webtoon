package webtoon.domains.manga.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import webtoon.domains.manga.dtos.MangaDto;
import webtoon.domains.manga.entities.MangaEntity;
import webtoon.domains.manga.enums.EMangaType;
import webtoon.domains.manga.models.MangaModel;

import java.util.List;

public interface IMangaService {

	MangaDto add(MangaModel model);
	MangaEntity getById(java.lang.Long id);


	boolean deleteById(java.lang.Long id);

	MangaDto update(MangaModel model);


    void setMangaType(java.lang.Long id, EMangaType type);

	Page<MangaEntity> filterBy(String s, Pageable page);

	Page<MangaDto> filter(Pageable pageable, Specification<MangaEntity> specs);

	MangaDto findById(Long id);

    List<MangaEntity> findAllOrder();

	Page<MangaDto> findAllById(Long id);
}
