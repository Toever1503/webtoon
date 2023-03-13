package webtoon.domains.manga.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import webtoon.domains.manga.entities.MangaAuthorRelationEntity;

public interface IMangaAuthorRelationRepository extends JpaRepository<MangaAuthorRelationEntity, Long>, JpaSpecificationExecutor<MangaAuthorRelationEntity> {

}
