package webtoon.domains.manga.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import webtoon.domains.manga.entities.MangaVolumeEntity;

import java.util.Optional;


@Repository
public interface IMangaVolumeRepository extends JpaRepository<MangaVolumeEntity, Long>, JpaSpecificationExecutor<MangaVolumeEntity>{

    @Query(value = "SELECT * FROM `manga_volume_entity` \n" +
            "WHERE manga_id = :mangaId\n" +
            "ORDER BY volume_index desc\n" +
            "LIMIT 0,1", nativeQuery = true)
    Optional<MangaVolumeEntity> getLastIndex(@Param("mangaId") Long mangaId);
}
