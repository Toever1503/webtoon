package webtoon.domains.manga.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import org.springframework.data.jpa.repository.Query;
import webtoon.domains.manga.entities.MangaChapterEntity;

import java.util.List;
import java.util.Optional;


public interface IMangaChapterRepository extends JpaRepository<MangaChapterEntity, Long>, JpaSpecificationExecutor<MangaChapterEntity>  {

    List<MangaChapterEntity> findByMangaVolumeId(Long id);

    @Query("SELECT MAX(chapterIndex) FROM MangaChapterEntity WHERE mangaVolume.manga.id = ?1")
    Optional<Long> getLastChapterIndex(Long mangaId);
}
