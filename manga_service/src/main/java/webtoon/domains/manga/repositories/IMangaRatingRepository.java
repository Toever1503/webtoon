package webtoon.domains.manga.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import webtoon.domains.manga.dtos.MangaDto;
import webtoon.domains.manga.entities.MangaRatingEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface IMangaRatingRepository extends JpaRepository<MangaRatingEntity, Long>, JpaSpecificationExecutor<MangaRatingEntity> {
    Page<MangaDto> findAllById(Long id, Pageable pageable);

    @Query("select count(a)as tongso,avg(a.rate) as xh from MangaRatingEntity a where a.mangaId = ?1")
    List<Map> getRateAvg(Long mangaId);

    @Query("select a from MangaRatingEntity a where a.mangaId =?1")
    List<MangaRatingEntity> findByMangaId(Long mangaId);

    @Query("select avg(a.rate) as rate from MangaRatingEntity a where a.mangaId = ?1")
    Double findByManga(Long mangaId);
}
