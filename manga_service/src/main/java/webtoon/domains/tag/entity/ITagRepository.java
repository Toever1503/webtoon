package webtoon.domains.tag.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ITagRepository extends JpaRepository<TagEntity, Long>, JpaSpecificationExecutor<TagEntity> {

    @Query(value = "SELECT * FROM hoc_pte.tbl_tags\n" +
            "where tag_name = convert(?1 using binary)", nativeQuery = true)
    Optional<TagEntity> findByTagName(String tagName);

}