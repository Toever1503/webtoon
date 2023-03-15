package webtoon.domains.manga.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import webtoon.domains.manga.entities.MangaGenreEntity;


public interface IMangaGenreRepository extends JpaRepository<MangaGenreEntity, Long>, JpaSpecificationExecutor<MangaGenreEntity>  {

}
