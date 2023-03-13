package webtoon.domains.tag.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ITagRepository extends JpaRepository<TagEntity, Long>, JpaSpecificationExecutor<TagEntity> {

    @Query("select t from TagEntity t")
    Optional<TagEntity> findByTagName(String tagName);
}