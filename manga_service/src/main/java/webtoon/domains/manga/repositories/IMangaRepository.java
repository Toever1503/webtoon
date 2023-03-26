package webtoon.domains.manga.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import webtoon.domains.manga.entities.Long;

public interface IMangaRepository extends JpaRepository<Long, java.lang.Long>, JpaSpecificationExecutor<Long> {
	
}
