package webtoon.domains.manga.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import webtoon.domains.manga.dtos.MangaChapterImageDto;
import webtoon.domains.manga.entities.MangaChapterImageEntity;
import webtoon.domains.manga.models.MangaChapterImageModel;
import webtoon.domains.manga.repositories.IMangaChapterImageRepository;
import webtoon.domains.manga.services.IMangaChapterImageService;

@Service
@Transactional
public class IMangaChapterImageServiceImpl implements IMangaChapterImageService {
	@Autowired
	private IMangaChapterImageRepository chapterImageRepository;

	@Override
	public MangaChapterImageDto add(MangaChapterImageModel model) {
		MangaChapterImageEntity entity = MangaChapterImageEntity.builder().mangaChapter(model.getMangaChapterId())
				.image(model.getImage()).imageIndex(model.getImageIndex()).build();
		this.chapterImageRepository.saveAndFlush(entity);
		return MangaChapterImageDto.builder().mangaChapterId(entity.getMangaChapter()).image(entity.getImage())
				.imageIndex(entity.getImageIndex()).build();
	}

	@Override
	public MangaChapterImageDto update(MangaChapterImageModel model) {
		MangaChapterImageEntity entity = this.getById(model.getId());
		entity.setImage(model.getImage());
		entity.setMangaChapter(model.getMangaChapterId());
		entity.setImageIndex(model.getImageIndex());
		chapterImageRepository.saveAndFlush(entity);

		return MangaChapterImageDto.builder().image(entity.getImage()).imageIndex(entity.getImageIndex())
				.mangaChapterId(entity.getMangaChapter()).build();
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
	
	@Override
	public Page<MangaChapterImageDto> filter(Pageable pageable,Specification<MangaChapterImageEntity> specs){
		return chapterImageRepository.findAll(specs, pageable).map(MangaChapterImageDto::toDto);
	}

	public MangaChapterImageEntity getById(Long id) {
		return this.chapterImageRepository.findById(id).orElseThrow(() -> new RuntimeException("22"));
	}

}
