package webtoon.main.domains.manga.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import webtoon.main.domains.manga.entities.MangaViewCountEntity;

public interface IMangaViewCountEntityRepository extends JpaRepository<MangaViewCountEntity, Long>, JpaSpecificationExecutor<MangaViewCountEntity> {
    @Query("select sum(mvc.count) from MangaViewCountEntity mvc where mvc.manga.id = ?1")
    Long countViewByMangaId(Long mangaId);

    @Query(value = "SELECT * FROM tbl_manga_view_count\n" +
            "where manga_id = :mangaId and created_at = current_date()", nativeQuery = true)
    MangaViewCountEntity findViewCountTodayForMangaId(@Param("mangaId") Long mangaId);
}
