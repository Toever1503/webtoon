package webtoon.domains.manga.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import webtoon.domains.manga.dtos.MangaDto;
import webtoon.domains.manga.entities.MangaEntity;
import webtoon.domains.manga.models.MangaModel;

public interface IMangaService {

	MangaDto add(MangaModel model);


	boolean deleteById(Long id);

	MangaDto update(MangaModel model);


	Page<MangaEntity> filter(String s, Pageable page);


	Optional<MangaEntity> findById(Long id);


}
