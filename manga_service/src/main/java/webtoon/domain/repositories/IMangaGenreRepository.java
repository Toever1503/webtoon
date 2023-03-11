package webtoon.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import webtoon.domain.entities.MangaGenreEntity;


public interface IMangaGenreRepository extends JpaRepository<MangaGenreEntity, Long>, JpaSpecificationExecutor<MangaGenreEntity>  {

}
