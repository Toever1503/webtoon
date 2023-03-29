package webtoon.comment.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import webtoon.comment.entities.CommentEntity;

@Repository
public interface ICommentRepository extends JpaRepository<CommentEntity,Long> {
}
