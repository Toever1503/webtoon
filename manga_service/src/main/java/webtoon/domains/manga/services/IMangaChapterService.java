package webtoon.domains.manga.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import org.springframework.web.multipart.MultipartFile;
import webtoon.domains.manga.dtos.MangaChapterDto;
import webtoon.domains.manga.entities.MangaChapterEntity;
import webtoon.domains.manga.entities.MangaChapterImageEntity;
import webtoon.domains.manga.models.MangaChapterModel;
import webtoon.domains.manga.models.MangaUploadChapterInput;

import java.util.List;

public interface IMangaChapterService {

	boolean deleteById(Long id);

	MangaChapterDto update(MangaChapterModel model);

	MangaChapterDto add(MangaChapterModel model);

	MangaChapterEntity getById(Long id);

	MangaChapterImageEntity getImageForChapter(Long chapterId);

	Page<MangaChapterDto> filter(Pageable pageable, Specification<MangaChapterEntity> specs);

    void createTextChapter(MangaUploadChapterInput input);

    void createImageChapter(MangaUploadChapterInput input, List<MultipartFile> multipartFiles);

    MangaChapterDto[] findNextPosts(Long chapterID, Long volumeId);
}
