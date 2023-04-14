package webtoon.account.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import webtoon.account.entities.AuthorityEntity;
import webtoon.account.entities.EAuthorityConstants;

public interface IAuthorityRepository extends JpaRepository<AuthorityEntity, Long> {

    AuthorityEntity findByAuthorityName(EAuthorityConstants authorityName);
}
