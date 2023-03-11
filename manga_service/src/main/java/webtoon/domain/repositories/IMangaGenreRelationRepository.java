package webtoon.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import webtoon.domain.entities.MangaGenreRelationEntity;


public interface IMangaGenreRelationRepository extends JpaRepository<MangaGenreRelationEntity, Long>, JpaSpecificationExecutor<MangaGenreRelationEntity>  {

}
