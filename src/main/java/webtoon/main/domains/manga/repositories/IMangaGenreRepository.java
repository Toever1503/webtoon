package webtoon.main.domains.manga.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import webtoon.main.domains.manga.entities.MangaGenreEntity;

import java.util.Optional;


public interface IMangaGenreRepository extends JpaRepository<MangaGenreEntity, Long>, JpaSpecificationExecutor<MangaGenreEntity>  {


    @Query(value = "SELECT * FROM tbl_manga_genre\n" +
            "where name = convert(?1 using binary)", nativeQuery = true)
    Optional<MangaGenreEntity> findByName(String name);
    @Query(value = "SELECT * FROM tbl_manga_genre\n" +
            "where slug = convert(?1 using binary)", nativeQuery = true)
    Optional<MangaGenreEntity> findBySlug(String slug);

    @Query(value = "SELECT count(manga_id) as total FROM tbl_manga_genre_relation\n" +
            "where genre_id = :genreId", nativeQuery = true)
    Long countTotalManga(@Param("genreId") Long genreId);
}
