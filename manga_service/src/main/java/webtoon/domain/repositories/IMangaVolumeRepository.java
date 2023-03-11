package webtoon.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import webtoon.domain.entities.MangaVolumeEntity;


public interface IMangaVolumeRepository extends JpaRepository<MangaVolumeEntity, Long>, JpaSpecificationExecutor<MangaVolumeEntity>{

}
