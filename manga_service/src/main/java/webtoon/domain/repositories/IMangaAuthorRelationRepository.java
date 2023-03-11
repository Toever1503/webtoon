package webtoon.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import webtoon.domain.entities.MangaAuthorRelationEntity;

public interface IMangaAuthorRelationRepository extends JpaRepository<MangaAuthorRelationEntity, Long>, JpaSpecificationExecutor<MangaAuthorRelationEntity> {

}
