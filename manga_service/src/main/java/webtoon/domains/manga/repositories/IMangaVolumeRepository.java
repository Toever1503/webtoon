package webtoon.domains.manga.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import webtoon.domains.manga.entities.MangaVolumeEntity;


public interface IMangaVolumeRepository extends JpaRepository<MangaVolumeEntity, Long>, JpaSpecificationExecutor<MangaVolumeEntity>{

}
