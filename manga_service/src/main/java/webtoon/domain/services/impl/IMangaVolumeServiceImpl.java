package webtoon.domain.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import webtoon.domain.dtos.MangaVolumeDto;
import webtoon.domain.entities.MangaVolumeEntity;
import webtoon.domain.models.MangaVolumeModel;
import webtoon.domain.repositories.IMangaVolumeRepository;
import webtoon.domain.services.IMangaVolumeService;

@Service
@Transactional
public class IMangaVolumeServiceImpl implements IMangaVolumeService {

	@Autowired
	private IMangaVolumeRepository mangaVolumeRepository;

	@Override
	public MangaVolumeDto add(MangaVolumeModel model) {
		MangaVolumeEntity entity = MangaVolumeEntity.builder().mangaId(model.getMangaId()).name(model.getName())
				.volumeIndex(model.getVolumeIndex()).build();
		mangaVolumeRepository.saveAndFlush(entity);
		return MangaVolumeDto.builder().mangaId(entity.getMangaId()).name(model.getName())
				.volumeIndex(model.getVolumeIndex()).build();
	}

	@Override
	public MangaVolumeDto update(MangaVolumeModel model) {
		MangaVolumeEntity entity = this.getById(model.getId());
		entity.setMangaId(model.getMangaId());
		entity.setName(model.getName());
		entity.setVolumeIndex(model.getVolumeIndex());
		mangaVolumeRepository.saveAndFlush(entity);
		return MangaVolumeDto.builder().mangaId(entity.getMangaId()).name(entity.getName())
				.volumeIndex(entity.getVolumeIndex()).build();
	}

	@Override
	public boolean deleteById(Long id) {
		try {
			mangaVolumeRepository.deleteById(id);
			return true;
		} catch (Exception e) {
			return false;
			// TODO: handle exception
		}
	}

	public MangaVolumeEntity getById(Long id) {
		return this.mangaVolumeRepository.findById(id).orElseThrow();
	}
}
