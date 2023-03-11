package webtoon.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import webtoon.domain.entities.MangaEntity;

public interface IMangaRepository extends JpaRepository<MangaEntity, Long>, JpaSpecificationExecutor<MangaEntity> {
	
}
