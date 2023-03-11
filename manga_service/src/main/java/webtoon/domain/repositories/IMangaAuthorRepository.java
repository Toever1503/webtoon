package webtoon.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import webtoon.domain.entities.MangaAuthorEntity;


public interface IMangaAuthorRepository extends JpaRepository<MangaAuthorEntity, Long>, JpaSpecificationExecutor<MangaAuthorEntity> {

}
