package webtoon.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import webtoon.domain.entities.MangaChapterEntity;


public interface IMangaChapterRepository extends JpaRepository<MangaChapterEntity, Long>, JpaSpecificationExecutor<MangaChapterEntity>  {

}
