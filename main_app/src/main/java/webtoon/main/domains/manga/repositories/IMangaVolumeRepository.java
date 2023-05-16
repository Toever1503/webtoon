package webtoon.main.domains.manga.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import webtoon.main.domains.manga.entities.MangaVolumeEntity;

import java.util.List;
import java.util.Optional;


@Repository
public interface IMangaVolumeRepository extends JpaRepository<MangaVolumeEntity, Long>, JpaSpecificationExecutor<MangaVolumeEntity> {

    @Query(value = "SELECT * FROM `manga_volume_entity` \n" +
            "WHERE manga_id = :mangaId\n" +
            "ORDER BY volume_index desc\n" +
            "LIMIT 0,1", nativeQuery = true)
    Optional<MangaVolumeEntity> getLastIndex(@Param("mangaId") Long mangaId);

    @Query("select p from MangaVolumeEntity  p where  p.manga.id = ?1")
    List<MangaVolumeEntity> findByMangaId(@Param("manga") Long manga);


    @Modifying
    @Query("UPDATE FROM MangaVolumeEntity v set v.volumeIndex = v.volumeIndex-1 where v.volumeIndex > ?1")
    void reindexVolumeAfterIndex(Integer index);

    @Modifying
    void deleteAllByMangaId(Long id);

    //tìm kiếm manga
    @Query("select p from MangaVolumeEntity p where p.manga.id =?1 order by p.volumeIndex asc ")
    MangaVolumeEntity finByMangaId(Long mangaId);

    @Modifying
    @Query("DELETE FROM MangaVolumeEntity v where v.id = ?1")
    void deleteVolById(Long id);

    Long countByMangaId(Long id);

//    @Modifying
//    @Query("DELETE FROM MangaVolumeEntity v where v.id = ?1")
//    void deleteById(Long id);
}
