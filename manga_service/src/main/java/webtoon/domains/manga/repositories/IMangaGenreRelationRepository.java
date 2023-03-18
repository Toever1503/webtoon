package webtoon.domains.manga.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import webtoon.domains.manga.entities.MangaGenreRelationEntity;


public interface IMangaGenreRelationRepository extends JpaRepository<MangaGenreRelationEntity, Long>, JpaSpecificationExecutor<MangaGenreRelationEntity>  {

}
