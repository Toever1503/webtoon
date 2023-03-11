package webtoon.domain.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import webtoon.domain.dtos.MangaChapterDto;
import webtoon.domain.entities.MangaChapterEntity;
import webtoon.domain.models.MangaChapterModel;
import webtoon.domain.repositories.IMangaChapterRepository;
import webtoon.domain.services.IMangaChapterService;

@Service
@Transactional
public class IMangaChapterServiceImpl implements IMangaChapterService {

	@Autowired
	private IMangaChapterRepository chapterRepository;

	@Override
	public MangaChapterDto add(MangaChapterModel model) {
		MangaChapterEntity chapterEntity = MangaChapterEntity.builder().name(model.getName())
				.mangaId(model.getMangaId()).content(model.getContent()).chapterIndex(model.getChapterIndex())
				.requiredVip(model.getRequiredVip()).build();
		this.chapterRepository.saveAndFlush(chapterEntity);
		return MangaChapterDto.builder().name(chapterEntity.getName()).mangaId(chapterEntity.getMangaId())
				.chapterIndex(chapterEntity.getChapterIndex()).content(chapterEntity.getContent())
				.requiredVip(chapterEntity.getRequiredVip()).build();
	}

	@Override
	public MangaChapterDto update(MangaChapterModel model) {

		MangaChapterEntity entity = this.getById(model.getId());
		entity.setChapterIndex(model.getChapterIndex());
		entity.setContent(model.getContent());
		entity.setMangaId(model.getMangaId());
		entity.setName(model.getName());
		entity.setRequiredVip(model.getRequiredVip());
		chapterRepository.saveAndFlush(entity);
		return MangaChapterDto.builder().name(entity.getName()).content(entity.getContent())
				.chapterIndex(entity.getChapterIndex()).mangaId(entity.getMangaId())
				.requiredVip(entity.getRequiredVip()).build();
	}

	@Override
	public boolean deleteById(Long id) {
		try {
			this.chapterRepository.deleteById(id);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}

	public MangaChapterEntity getById(Long id) {
		return this.chapterRepository.findById(id).orElseThrow();
	}

}
