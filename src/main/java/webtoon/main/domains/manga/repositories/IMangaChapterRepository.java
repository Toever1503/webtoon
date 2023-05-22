package webtoon.main.domains.manga.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import webtoon.main.domains.manga.dtos.MangaChapterDto;
import webtoon.main.domains.manga.entities.MangaChapterEntity;

import java.util.List;
import java.util.Optional;


public interface IMangaChapterRepository extends JpaRepository<MangaChapterEntity, Long>, JpaSpecificationExecutor<MangaChapterEntity> {

    List<MangaChapterEntity> findByMangaVolumeId(Long id);

    @Query("SELECT MAX(chapterIndex) FROM MangaChapterEntity WHERE mangaVolume.id = ?1")
    Optional<Long> getLastChapterIndexForVolType(Long mangaId);


    @Query(value = "SELECT * FROM `tbl_manga_chapter_entity`\n" +
            "where manga_id = :mangaId\n" +
            "ORDER BY chapter_index desc\n" +
            "LIMIT 0,1", nativeQuery = true)
    Optional<MangaChapterEntity> getLastChapterIndexForChapType(@Param("mangaId") Long mangaId);

    void deleteALlByMangaId(Long id);
    @Modifying
    @Query("DELETE FROM MangaChapterEntity c where c.mangaVolume.id = ?1")
    void deleteALlByMangaVolumeId(Long id);

    @Query("select p from MangaChapterEntity p where p.id > ?1 and p.mangaVolume.manga.id = ?2")
    List<MangaChapterEntity> findNextChapterForDisplayVolType(Long chapterID, Long mangaId, Pageable page);

    @Query("select p from MangaChapterEntity p where p.id < ?1 and p.mangaVolume.manga.id = ?2")
    List<MangaChapterEntity> findPrevChapterForDisplayVolType(Long chapterID, Long mangaId, Pageable page);



    @Query("select p from MangaChapterEntity p where p.id > ?1 and p.manga.id = ?2")
    List<MangaChapterEntity> findNextChapterForDisplayChapType(Long chapterID, Long mangaId, Pageable page);

    @Query("select p from MangaChapterEntity p where p.id < ?1 and p.manga.id = ?2")
    List<MangaChapterEntity> findPrevChapterForDisplayChapType(Long chapterID, Long mangaId, Pageable page);

    @Modifying
    @Query("UPDATE  FROM MangaChapterEntity c set c.chapterIndex = c.chapterIndex-1 where c.chapterIndex > ?1 and c.manga.id = ?2")
    void reindexChapterAfterIndex(Integer index, Long mangaId);

    @Modifying
    @Query("UPDATE  FROM MangaChapterEntity c set c.chapterIndex = c.chapterIndex-1 where c.chapterIndex > ?1 and c.mangaVolume.id = ?2")
    void reindexChapterAfterIndexForVol(Integer index, Long volId);

    @Query("select p from MangaChapterEntity  p where  p.mangaVolume.id = ?1")
    List<MangaChapterEntity> findByVolumeId(@Param("id") Long id);

    List<MangaChapterDto> findAllById(Long id, Pageable pageable);

    @Query("select p from MangaChapterEntity p where p.manga.id = ?1")
    List<MangaChapterEntity> findByMangaId(@Param("id") Long id);

    //tìm kiếm object manga
    @Query("select p from MangaChapterEntity p where p.manga.id = ?1 order by p.chapterIndex asc ")
    MangaChapterEntity findByMangId(Long id);

    Long countByMangaId(Long id);


    @Modifying
    @Query(value = "DELETE FROM `tbl_manga_chapter_entity` WHERE id = :id", nativeQuery = true)
    void deleteById(@Param(("id")) Long id);

    @Query(value = "SELECT * FROM stovizkj_webtoon_final.tbl_manga_chapter_entity\n" +
            "where manga_volume_id = :volId\n" +
            "limit 0,1", nativeQuery = true)
    MangaChapterEntity getFirstIdForVolType(@Param("volId") Long volId);
    @Query(value = "SELECT * FROM stovizkj_webtoon_final.tbl_manga_chapter_entity\n" +
            "where manga_id = :mangaId\n" +
            "limit 0,1", nativeQuery = true)
    MangaChapterEntity getFirstIdForChapType(@Param("mangaId") Long mangaId);


    @Query(value = "SELECT c.* FROM tbl_manga_chapter_entity as c\n" +
            "join manga_volume_entity as  v on v.id = c.manga_volume_id \n" +
            "where c.required_vip = 0 and v.manga_id = :mangaId\n" +
            "limit 0, 5", nativeQuery = true)
    List<MangaChapterEntity> getFreeChaptersForVolType(@Param("mangaId") Long mangaId);

    @Query(value = "SELECT * FROM tbl_manga_chapter_entity \n" +
            "where required_vip = 0 and manga_id = :mangaId\n" +
            "limit 0, 5", nativeQuery = true)
    List<MangaChapterEntity> getFreeChaptersForChapType(@Param("mangaId") Long mangaId);
}
