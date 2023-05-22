package webtoon.main.domains.manga.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import webtoon.main.domains.manga.entities.MangaAuthorEntity;
import webtoon.main.domains.manga.entities.MangaGenreEntity;

import java.util.Optional;


public interface IMangaAuthorRepository extends JpaRepository<MangaAuthorEntity, Long>, JpaSpecificationExecutor<MangaAuthorEntity> {

    @Query(value = "SELECT * FROM tbl_manga_author\n" +
            "where name = convert(?1 using binary)", nativeQuery = true)
    Optional<MangaAuthorEntity> findByName(String name);
    @Query(value = "SELECT * FROM tbl_manga_author\n" +
            "where slug = convert(?1 using binary)", nativeQuery = true)
    Optional<MangaAuthorEntity> findBySlug(String slug);

    @Query(value = "SELECT count(manga_id) as total FROM tbl_manga_author_relation\n" +
            "where author_id = :authorId", nativeQuery = true)
    Long countTotalMangaByAuthorId(@Param("authorId") Long authorId);
}
