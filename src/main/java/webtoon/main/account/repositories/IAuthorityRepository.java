package webtoon.main.account.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import webtoon.main.account.entities.AuthorityEntity;
import webtoon.main.account.entities.EAuthorityConstants;

public interface IAuthorityRepository extends JpaRepository<AuthorityEntity, Long> {

    AuthorityEntity findByAuthorityName(EAuthorityConstants authorityName);
}
