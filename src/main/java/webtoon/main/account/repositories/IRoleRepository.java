package webtoon.main.account.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import webtoon.main.account.entities.RoleEntity;

@Repository
public interface IRoleRepository extends JpaRepository<RoleEntity, Long> {
}
