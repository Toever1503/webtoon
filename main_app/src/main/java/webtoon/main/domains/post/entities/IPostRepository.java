package webtoon.main.domains.post.entities;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPostRepository extends JpaRepository<PostEntity, Long>, JpaSpecificationExecutor<PostEntity> {
    @Query("select p from PostEntity p where p.id > ?1 and p.category.id = ?2")
    List<PostEntity> findNextPost(Long postID, Long categoryId, Pageable page);

    @Query("select p from PostEntity p where p.id < ?1 and p.category.id = ?2")
    List<PostEntity> findPrevPost(Long postID, Long categoryId, Pageable page);
}
