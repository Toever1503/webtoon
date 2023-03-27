package webtoon.domains.manga.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import webtoon.domains.manga.entities.MangaEntity;

public interface IMangaRepository extends JpaRepository<MangaEntity, Long>, JpaSpecificationExecutor<MangaEntity> {
	
}
