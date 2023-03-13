package webtoon.domains.manga.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import webtoon.domains.manga.dtos.MangaAuthorDto;
import webtoon.domains.manga.entities.MangaAuthorEntity;
import webtoon.domains.manga.models.MangaAuthorModel;
import webtoon.domains.manga.repositories.IMangaAuthorRepository;
import webtoon.domains.manga.services.IMangaAuthorService;

@Service
@Transactional
public class IMangaAuthorServiceImpl implements IMangaAuthorService {

	@Autowired
	private IMangaAuthorRepository mangaAuthorRepository;

	@Override
	public MangaAuthorDto add(MangaAuthorModel model) {
		MangaAuthorEntity authorEntity = MangaAuthorEntity.builder().name(model.getName()).build();
		this.mangaAuthorRepository.saveAndFlush(authorEntity);
		return MangaAuthorDto.builder().name(authorEntity.getName()).build();
	}

	@Override
	public MangaAuthorDto update(MangaAuthorModel model) {
		MangaAuthorEntity authorEntity = this.getById(model.getId());
		authorEntity.setName(model.getName());
		mangaAuthorRepository.saveAndFlush(authorEntity);
		return MangaAuthorDto.builder().name(authorEntity.getName()).build();
	}

	@Override
	public MangaAuthorEntity getById(Long id) {
		return this.mangaAuthorRepository.findById(id).orElseThrow();
	}

	@Override
	public boolean deleteById(Long id) {
		try {
			this.mangaAuthorRepository.deleteById(id);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}

}
