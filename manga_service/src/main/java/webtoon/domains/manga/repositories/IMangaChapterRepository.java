package webtoon.domains.manga.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import webtoon.domains.manga.dtos.MangaChapterDto;
import webtoon.domains.manga.entities.MangaChapterEntity;

import java.util.List;
import java.util.Optional;


public interface IMangaChapterRepository extends JpaRepository<MangaChapterEntity, Long>, JpaSpecificationExecutor<MangaChapterEntity> {

    List<MangaChapterEntity> findByMangaVolumeId(Long id);

    @Query("SELECT MAX(chapterIndex) FROM MangaChapterEntity WHERE mangaVolume.id = ?1")
    Optional<Long> getLastChapterIndexForVolType(Long mangaId);


    @Query("SELECT MAX(c.chapterIndex) FROM MangaChapterEntity c WHERE c.manga.id = ?1")
    Optional<Long> getLastChapterIndexForChapType(Long mangaId);

    void deleteALlByMangaId(Long id);

    @Query("select p from MangaChapterEntity p where p.id > ?1 and p.mangaVolume.manga.id = ?2")
    List<MangaChapterEntity> findNextChapterForDisplayVolType(Long chapterID, Long mangaId, Pageable page);

    @Query("select p from MangaChapterEntity p where p.id < ?1 and p.mangaVolume.manga.id = ?2")
    List<MangaChapterEntity> findPrevChapterForDisplayVolType(Long chapterID, Long mangaId, Pageable page);



    @Query("select p from MangaChapterEntity p where p.id > ?1 and p.manga.id = ?2")
    List<MangaChapterEntity> findNextChapterForDisplayChapType(Long chapterID, Long mangaId, Pageable page);

    @Query("select p from MangaChapterEntity p where p.id < ?1 and p.manga.id = ?2")
    List<MangaChapterEntity> findPrevChapterForDisplayChapType(Long chapterID, Long mangaId, Pageable page);

    @Modifying
    @Query("UPDATE  FROM MangaChapterEntity c set c.chapterIndex = c.chapterIndex-1 where c.chapterIndex > ?1")
    void reindexChapterAfterIndex(Integer index);

    @Query("select p from MangaChapterEntity  p where  p.mangaVolume.id = ?1")
    List<MangaChapterEntity> findByVolumeId(@Param("id") Long id);

    List<MangaChapterDto> findAllById(Long id, Pageable pageable);

    @Query("select p from MangaChapterEntity p where p.manga.id = ?1")
    List<MangaChapterEntity> findByMangaId(@Param("id") Long id);

    //tìm kiếm object manga
    @Query("select p from MangaChapterEntity p where p.manga.id = ?1 order by p.chapterIndex asc ")
    MangaChapterEntity findByMangId(Long id);
}
