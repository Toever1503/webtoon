package webtoon.domains.manga.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import webtoon.domains.manga.dtos.MangaVolumeDto;
import webtoon.domains.manga.entities.MangaVolumeEntity;
import webtoon.domains.manga.models.MangaVolumeModel;

public interface IMangaVolumeService {

	boolean deleteById(Long id);

	MangaVolumeDto update(MangaVolumeModel model);

	MangaVolumeDto add(MangaVolumeModel model);

	Page<MangaVolumeDto> filter(Pageable pageable, Specification<MangaVolumeEntity> specs);

}
