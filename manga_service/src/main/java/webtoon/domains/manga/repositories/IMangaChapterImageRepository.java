package webtoon.domains.manga.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import webtoon.domains.manga.entities.MangaChapterImageEntity;


public interface IMangaChapterImageRepository extends JpaRepository<MangaChapterImageEntity, Long>, JpaSpecificationExecutor<MangaChapterImageEntity> {

}
