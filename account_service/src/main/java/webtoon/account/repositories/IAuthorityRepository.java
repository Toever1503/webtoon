package webtoon.account.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import webtoon.account.entities.AuthorityEntity;

public interface IAuthorityRepository extends JpaRepository<AuthorityEntity, Long> {

    AuthorityEntity findByAuthorityName(String authorityName);
}
