package webtoon.domains.manga.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import org.springframework.stereotype.Repository;
import webtoon.domains.manga.entities.MangaVolumeEntity;

@Repository
public interface IMangaVolumeRepository extends JpaRepository<MangaVolumeEntity, Long>, JpaSpecificationExecutor<MangaVolumeEntity>{

}
