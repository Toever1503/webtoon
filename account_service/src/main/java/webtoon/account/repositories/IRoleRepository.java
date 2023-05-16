package webtoon.account.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import webtoon.account.entities.RoleEntity;

@Repository
public interface IRoleRepository extends JpaRepository<RoleEntity, Long> {
}
