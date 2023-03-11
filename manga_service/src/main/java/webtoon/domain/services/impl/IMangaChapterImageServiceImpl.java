package webtoon.domain.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import webtoon.domain.dtos.MangaChapterImageDto;
import webtoon.domain.entities.MangaChapterImageEntity;
import webtoon.domain.models.MangaChapterImageModel;
import webtoon.domain.repositories.IMangaChapterImageRepository;
import webtoon.domain.services.IMangaChapterImageService;

@Service
@Transactional
public class IMangaChapterImageServiceImpl implements IMangaChapterImageService {
	@Autowired
	private IMangaChapterImageRepository chapterImageRepository;

	@Override
	public MangaChapterImageDto add(MangaChapterImageModel model) {
		MangaChapterImageEntity entity = MangaChapterImageEntity.builder().mangaChapterId(model.getMangaChapterId())
				.image(model.getImage()).imageIndex(model.getImageIndex()).build();
		this.chapterImageRepository.saveAndFlush(entity);
		return MangaChapterImageDto.builder().mangaChapterId(entity.getMangaChapterId()).image(entity.getImage())
				.imageIndex(entity.getImageIndex()).build();
	}

	@Override
	public MangaChapterImageDto update(MangaChapterImageModel model) {
		MangaChapterImageEntity entity = this.getById(model.getId());
		entity.setImage(model.getImage());
		entity.setMangaChapterId(model.getMangaChapterId());
		entity.setImageIndex(model.getImageIndex());
		chapterImageRepository.saveAndFlush(entity);

		return MangaChapterImageDto.builder().image(entity.getImage()).imageIndex(entity.getImageIndex())
				.mangaChapterId(entity.getMangaChapterId()).build();
	}

	@Override
	public boolean deleteById(Long id) {
		try {
			this.chapterImageRepository.deleteById(id);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}

	public MangaChapterImageEntity getById(Long id) {
		return this.chapterImageRepository.findById(id).orElseThrow();
	}

}
