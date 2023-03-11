package webtoon.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import webtoon.domain.entities.MangaChapterImageEntity;


public interface IMangaChapterImageRepository extends JpaRepository<MangaChapterImageEntity, Long>, JpaSpecificationExecutor<MangaChapterImageEntity>  {

}
