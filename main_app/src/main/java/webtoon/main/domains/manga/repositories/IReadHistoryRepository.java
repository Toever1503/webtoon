package webtoon.main.domains.manga.repositories;

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
    ReadHistory findAllByManga( Long mangaEntity );

    @Query("SELECT a FROM ReadHistory a where a.mangaEntity = ?1 and a.createdBy = ?2 ")
    ReadHistory findAllByCBAndMG( Long CreatByID, Long MangaID );

}
