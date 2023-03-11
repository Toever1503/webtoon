package webtoon.domain.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import webtoon.domain.dtos.MangaGenreDto;
import webtoon.domain.entities.MangaGenreEntity;
import webtoon.domain.models.MangaGenreModel;
import webtoon.domain.repositories.IMangaGenreRepository;
import webtoon.domain.services.IMangaGenreService;

@Service
@Transactional
public class IMangaGenreServiceImpl implements IMangaGenreService {

	@Autowired
	private IMangaGenreRepository genreRepository;

	@Override
	public MangaGenreDto add(MangaGenreModel model) {
		MangaGenreEntity entity = MangaGenreEntity.builder().name(model.getName()).slug(model.getSlug()).build();
		genreRepository.saveAndFlush(entity);
		return MangaGenreDto.builder().name(entity.getName()).slug(entity.getSlug()).build();
	}

	@Override
	public MangaGenreDto update(MangaGenreModel model) {
		MangaGenreEntity entity = this.getById(model.getId());
		entity.setName(model.getName());
		entity.setSlug(model.getSlug());
		genreRepository.saveAndFlush(entity);
		return MangaGenreDto.builder().name(entity.getName()).slug(entity.getSlug()).build();
	}

	@Override
	public boolean deleteById(Long id) {
		try {
			genreRepository.deleteById(id);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}

	public MangaGenreEntity getById(Long id) {
		return this.genreRepository.findById(id).orElseThrow();
	}
}
