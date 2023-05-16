package webtoon.domains.manga.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import webtoon.domains.manga.entities.MangaChapterImageEntity;

import java.util.List;


public interface IMangaChapterImageRepository extends JpaRepository<MangaChapterImageEntity, Long>, JpaSpecificationExecutor<MangaChapterImageEntity> {

    List<MangaChapterImageEntity> findAllByIdNotIn(List<Long> oldImageIds);

    @Modifying
    @Query(value = "DELETE FROM `tbl_manga_chapter_image_entity` WHERE manga_chapter_id = :id", nativeQuery = true)
    void deleteByChapterId(@Param("id") Long id);
}
