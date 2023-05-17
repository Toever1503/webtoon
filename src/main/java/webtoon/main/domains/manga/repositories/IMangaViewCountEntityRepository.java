package webtoon.main.domains.manga.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import webtoon.main.domains.manga.entities.MangaViewCountEntity;

public interface IMangaViewCountEntityRepository extends JpaRepository<MangaViewCountEntity, Long>, JpaSpecificationExecutor<MangaViewCountEntity> {
    @Query("select sum(mvc.count) from MangaViewCountEntity mvc where mvc.manga.id = ?1")
    Long countViewByMangaId(Long mangaId);

    @Query("select mvc from MangaViewCountEntity mvc where mvc.manga.id = ?1 and mvc.createdAt = current_date")
    MangaViewCountEntity findViewCountTodayForMangaId(Long mangaId);
}
