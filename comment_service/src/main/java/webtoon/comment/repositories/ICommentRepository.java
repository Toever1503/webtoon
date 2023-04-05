package webtoon.comment.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import webtoon.comment.entities.CommentEntity;

import java.util.List;

@Repository
public interface ICommentRepository extends JpaRepository<CommentEntity, Long>, JpaSpecificationExecutor<CommentEntity> {
    @Query(value ="select * from tbl_comment where parent_id = ?1 ORDER BY id ASC", nativeQuery = true)
    List<CommentEntity> findAllByParentId(Long parentId);
}
