package webtoon.domains.manga.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import webtoon.domains.manga.dtos.MangaGenreRelationDto;
import webtoon.domains.manga.entities.MangaGenreRelationEntity;
import webtoon.domains.manga.models.MangaGenreRelationModel;
import webtoon.domains.manga.repositories.IMangaGenreRelationRepository;
import webtoon.domains.manga.services.IMangaGenreRelationService;
import webtoon.domains.manga.services.IMangaService;

@Service
@Transactional
public class IMangaGenreRelationServiceImpl implements IMangaGenreRelationService {

	@Autowired
	private IMangaGenreRelationRepository genreRelationRepository;

	@Autowired
	private IMangaService mangaService;

	@Override
	public MangaGenreRelationDto add(MangaGenreRelationModel model) {
		MangaGenreRelationEntity entity = MangaGenreRelationEntity.builder().genreId(model.getGenreId())
				.mangaId(this.mangaService.getById(model.getMangaId())).build();
		genreRelationRepository.saveAndFlush(entity);
		return MangaGenreRelationDto.builder().genreId(entity.getGenreId()).mangaId(entity.getMangaId().getId()).build();
	}

	@Override
	public MangaGenreRelationDto update(MangaGenreRelationModel model) {
		MangaGenreRelationEntity entity = this.getById(model.getId());
		entity.setGenreId(model.getGenreId());
		entity.setMangaId(this.mangaService.getById(model.getMangaId()));
		genreRelationRepository.saveAndFlush(entity);
		return MangaGenreRelationDto.builder().genreId(entity.getGenreId()).mangaId(entity.getMangaId().getId()).build();

	}

	@Override
	public boolean deleteById(Long id) {
		try {
			genreRelationRepository.deleteById(id);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}
	
	@Override
	public Page<MangaGenreRelationDto> filter(Pageable pageable,Specification<MangaGenreRelationEntity> specs){
		return genreRelationRepository.findAll(specs, pageable).map(MangaGenreRelationDto::toDto);
	}

	public MangaGenreRelationEntity getById(Long id) {
		return this.genreRelationRepository.findById(id).orElseThrow(() -> new RuntimeException("22"));
	}
}
