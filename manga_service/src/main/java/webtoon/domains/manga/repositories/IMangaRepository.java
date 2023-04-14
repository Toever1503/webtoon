package webtoon.domains.manga.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import webtoon.domains.manga.entities.MangaEntity;
import webtoon.domains.manga.dtos.MangaDto;

import java.util.List;

public interface IMangaRepository extends JpaRepository<MangaEntity, Long>, JpaSpecificationExecutor<MangaEntity> {


    Page<MangaDto> findAllById(Long id, Pageable pageable);

    @Query(value = "SELECT status, COUNT(id) as total FROM `tbl_manga_entity` \n" +
            "GROUP BY status", nativeQuery = true)
    List<Object[]> calculateTotalMangaEachStatus(@Param(value = "q") String q);
}
