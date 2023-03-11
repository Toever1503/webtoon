package webtoon.domain.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import webtoon.domain.dtos.MangaGenreRelationDto;
import webtoon.domain.entities.MangaGenreRelationEntity;
import webtoon.domain.models.MangaGenreRelationModel;
import webtoon.domain.repositories.IMangaGenreRelationRepository;
import webtoon.domain.services.IMangaGenreRelationService;

@Service
@Transactional
public class IMangaGenreRelationServiceImpl implements IMangaGenreRelationService {

	@Autowired
	private IMangaGenreRelationRepository genreRelationRepository;

	@Override
	public MangaGenreRelationDto add(MangaGenreRelationModel model) {
		MangaGenreRelationEntity entity = MangaGenreRelationEntity.builder().genreId(model.getGenreId())
				.mangaId(model.getMangaId()).build();
		genreRelationRepository.saveAndFlush(entity);
		return MangaGenreRelationDto.builder().genreId(entity.getGenreId()).mangaId(entity.getMangaId()).build();
	}

	@Override
	public MangaGenreRelationDto update(MangaGenreRelationModel model) {
		MangaGenreRelationEntity entity = this.getById(model.getId());
		entity.setGenreId(model.getGenreId());
		entity.setMangaId(model.getMangaId());
		genreRelationRepository.saveAndFlush(entity);
		return MangaGenreRelationDto.builder().genreId(entity.getGenreId()).mangaId(entity.getMangaId()).build();

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

	public MangaGenreRelationEntity getById(Long id) {
		return this.genreRelationRepository.findById(id).orElseThrow();
	}
}
