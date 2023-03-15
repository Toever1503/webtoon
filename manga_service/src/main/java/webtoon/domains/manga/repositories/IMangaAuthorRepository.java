package webtoon.domains.manga.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import webtoon.domains.manga.entities.MangaAuthorEntity;


public interface IMangaAuthorRepository extends JpaRepository<MangaAuthorEntity, Long>, JpaSpecificationExecutor<MangaAuthorEntity> {

}
