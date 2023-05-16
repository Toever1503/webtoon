package webtoon.main.storage.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import webtoon.main.storage.domain.entities.FileEntity;

@Repository
public interface IFileRepositoty extends JpaRepository<FileEntity, Long>, JpaSpecificationExecutor<FileEntity> {

}
