package webtoon.domains.manga.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import org.springframework.data.jpa.repository.Query;
import webtoon.domains.manga.entities.MangaChapterEntity;

import java.util.List;


public interface IMangaChapterRepository extends JpaRepository<MangaChapterEntity, Long>, JpaSpecificationExecutor<MangaChapterEntity>  {

    @Query("select p from MangaChapterEntity p where p.id > ?1 and p.mangaVolume.id = ?2")
    List<MangaChapterEntity> findNextchapter(Long chapterID, Long volumeId, Pageable page);

    @Query("select p from MangaChapterEntity p where p.id < ?1 and p.mangaVolume.id = ?2")
    List<MangaChapterEntity> findPrevchapter(Long chapterID, Long volumeId, Pageable page);
}
