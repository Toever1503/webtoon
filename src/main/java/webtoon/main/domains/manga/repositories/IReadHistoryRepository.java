package webtoon.main.domains.manga.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import webtoon.main.domains.manga.entities.ReadHistory;

import java.util.List;
import java.util.Optional;

@Repository
public interface IReadHistoryRepository extends JpaRepository<ReadHistory, Long>, JpaSpecificationExecutor<ReadHistory> {
    @Query("SELECT a FROM ReadHistory a where a.mangaEntity = ?1 ")
    ReadHistory findAllByManga(Long mangaEntity);

    @Query("SELECT a FROM ReadHistory a where a.mangaEntity = ?2 and a.createdBy = ?1 ")
    ReadHistory findAllByCBAndMG(Long CreatByID, Long MangaID);

    @Query(value = "SELECT * FROM tbl_read_history where created_by = ?1 ORDER BY created_date DESC", nativeQuery = true)
    List<ReadHistory> findAllByCreatedBy(Long CreatById);

    @Query("SELECT a FROM ReadHistory a where a.createdBy = ?1 ")
    Page<ReadHistory> findAllByCreatedByForUser(Long userId, Pageable pageable);
}
