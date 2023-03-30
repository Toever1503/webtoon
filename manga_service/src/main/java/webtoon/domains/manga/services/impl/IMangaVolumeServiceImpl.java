package webtoon.domains.manga.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import webtoon.domains.manga.dtos.MangaVolumeDto;
import webtoon.domains.manga.entities.MangaVolumeEntity;
import webtoon.domains.manga.models.MangaVolumeModel;
import webtoon.domains.manga.repositories.IMangaVolumeRepository;
import webtoon.domains.manga.services.IMangaService;
import webtoon.domains.manga.services.IMangaVolumeService;

@Service
@Transactional
public class IMangaVolumeServiceImpl implements IMangaVolumeService {

	@Autowired
	private IMangaVolumeRepository mangaVolumeRepository;

	@Autowired
	private IMangaService mangaService;

	@Override
	public MangaVolumeDto add(MangaVolumeModel model) {
		MangaVolumeEntity entity = MangaVolumeEntity.builder()
				.manga(mangaService.getById(model.getMangaId()))
				.name(model.getName())
				.volumeIndex(model.getVolumeIndex()).build();
		mangaVolumeRepository.saveAndFlush(entity);
		return MangaVolumeDto.builder().id(entity.getId()).mangaId(entity.getManga().getId()).name(model.getName())
				.volumeIndex(model.getVolumeIndex()).build();
	}

	@Override
	public MangaVolumeDto update(MangaVolumeModel model) {
		MangaVolumeEntity entity = this.getById(model.getId());
		entity.setManga(this.mangaService.getById(model.getMangaId()));
		entity.setName(model.getName());
		entity.setVolumeIndex(model.getVolumeIndex());
		mangaVolumeRepository.saveAndFlush(entity);
		return MangaVolumeDto.builder().id(entity.getId()).mangaId(entity.getManga().getId()).name(entity.getName())
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
	
	@Override
	public Page<MangaVolumeDto> filter(Pageable pageable,Specification<MangaVolumeEntity> specs){
		return mangaVolumeRepository.findAll(specs, pageable).map(MangaVolumeDto::toDto);
	}

	@Override
	public MangaVolumeEntity getById(Long id) {
		return this.mangaVolumeRepository.findById(id).orElseThrow(() -> new RuntimeException("22"));
	}

	@Override
	public MangaVolumeDto findById(Long id){
		return MangaVolumeDto.toDto(this.getById(id));
	}

	@Override
	public Page<MangaVolumeEntity> filterBy(String s, Pageable page){
		return this.mangaVolumeRepository.findAll((root, query, cb) -> {
			return cb.like(root.get("name"),"%" + s + "%");
		},page);
	}

}
