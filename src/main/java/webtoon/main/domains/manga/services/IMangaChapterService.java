package webtoon.main.domains.manga.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import org.springframework.web.multipart.MultipartFile;
import webtoon.main.domains.manga.dtos.MangaChapterDto;
import webtoon.main.domains.manga.entities.MangaChapterImageEntity;
import webtoon.main.domains.manga.models.MangaChapterFilterInput;
import webtoon.main.domains.manga.models.MangaChapterModel;
import webtoon.main.domains.manga.models.MangaUploadChapterInput;
import webtoon.main.domains.manga.entities.MangaChapterEntity;

import java.util.List;

public interface IMangaChapterService {

	boolean deleteById(Long id);

	MangaChapterDto update(MangaChapterModel model);

	MangaChapterDto add(MangaChapterModel model);

	MangaChapterEntity getById(Long id);

	MangaChapterImageEntity getImageForChapter(Long chapterId);

	Page<MangaChapterDto> filter(Pageable pageable, Specification<MangaChapterEntity> specs);

	MangaChapterDto saveTextChapter(MangaUploadChapterInput input);

	MangaChapterDto saveImageChapter(MangaUploadChapterInput input, List<MultipartFile> multipartFiles);

    List<MangaChapterDto> getAllByVolumeId(Long id);

	MangaChapterDto[] findNextPrevChapterForDisplayVolType(Long chapterID, Long mangaId);

    MangaChapterDto[] findNextPrevChapterForDisplayChapType(Long chapterID, Long mangaId);

    Page<MangaChapterDto> filterChapter(Pageable pageable, MangaChapterFilterInput input);

    MangaChapterDto findById(Long id);

    MangaChapterDto getLastChapterIndexForChapType(Long mangaId);
    Long getLastChapterIndexForVolType(Long volumeId);

    MangaChapterEntity getFirstIdForVolType(Long volId);
    List<MangaChapterEntity> findByVolume(Long volume);

    List<MangaChapterEntity> findAll();

	List<MangaChapterDto> findAllByVolume(Long volId);

    List<MangaChapterDto> findAllById(Long id);

    List<MangaChapterEntity> findAllByMangaId(Long id);

    MangaChapterEntity finByMangaId(Long id);

    Long countTotalChapterForMangaTypeVol(Long id);

    MangaChapterEntity getFirstIdForChapType(Long mangaId);

    List<MangaChapterEntity> getFreeChaptersForVolType(Long mangaId);

    List<MangaChapterEntity> getFreeChaptersForChapType(Long mangaId);
}
