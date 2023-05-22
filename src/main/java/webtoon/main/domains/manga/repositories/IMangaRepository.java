package webtoon.main.domains.manga.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import webtoon.main.domains.manga.entities.MangaEntity;
import webtoon.main.domains.manga.dtos.MangaDto;
import webtoon.main.domains.manga.entities.MangaGenreEntity;

import java.util.List;
import java.util.Set;

public interface IMangaRepository extends JpaRepository<MangaEntity, Long>, JpaSpecificationExecutor<MangaEntity> {


    Page<MangaDto> findAllById(Long id, Pageable pageable);

    @Query(value = "SELECT status, COUNT(id) as total FROM `tbl_manga_entity` \n" +
            "GROUP BY status", nativeQuery = true)
    List<Object[]> calculateTotalMangaEachStatus(@Param(value = "q") String q);

    @Query("select p from MangaEntity p where p.id =?1 and p.createdBy.id = ?2")
    MangaEntity getByIdAndCb(Long id, Long createId);

    @Query("select p.rating from MangaEntity p where  p.id =?1")
    Double getRatingManga(Long id);

    @Query("select p from MangaEntity p where p.id =?1 ")
    MangaEntity getByMangaId(Long id);

    @Query(value = "SELECT distinct a.* FROM tbl_manga_entity a LEFT JOIN tbl_manga_genre_relation b ON b.manga_id = a.id\n" +
            " WHERE b.genre_id in :ids and a.status != 'DRAFTED' and a.deleted_at is null limit 0,14",
            nativeQuery = true)
    List<MangaEntity> findByGenresIn(@Param("ids") List<Long> ids);

    @Query("select distinct m.releaseYear from MangaEntity m where m.releaseYear is not null")
    List<Integer> findAllReleaseYear();
}
