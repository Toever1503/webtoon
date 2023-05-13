package webtoon.domains.manga.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import webtoon.domains.manga.dtos.MangaVolumeDto;
import webtoon.domains.manga.entities.MangaVolumeEntity;
import webtoon.domains.manga.models.MangaVolumeFilterInput;
import webtoon.domains.manga.models.MangaVolumeModel;

import java.util.List;

public interface IMangaVolumeService {

	boolean deleteById(Long id);

	MangaVolumeDto update(MangaVolumeModel model);

	MangaVolumeDto add(MangaVolumeModel model);

	Page<MangaVolumeDto> filter(Pageable pageable, Specification<MangaVolumeEntity> specs);
	Page<MangaVolumeEntity> filterEntity(Pageable pageable, Specification<MangaVolumeEntity> specs);

    MangaVolumeEntity getById(Long id);

	List<MangaVolumeEntity> findAll();

	MangaVolumeDto findById(Long id);

    Page<MangaVolumeEntity> filterBy(String s, Pageable page);

    List<MangaVolumeEntity> findByManga(Long id);

    Page<MangaVolumeDto> filterVolume(Pageable pageable, MangaVolumeFilterInput input);

	MangaVolumeDto getLastVolIndex(Long mangaId);

	MangaVolumeEntity finByMangaId(Long mangaId);

    Long countTotalVol(Long id);
}
