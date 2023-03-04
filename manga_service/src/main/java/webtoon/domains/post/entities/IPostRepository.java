package webtoon.domains.post.entities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IPostRepository extends JpaRepository<PostEntity, Long>, JpaSpecificationExecutor<PostEntity> {

}
