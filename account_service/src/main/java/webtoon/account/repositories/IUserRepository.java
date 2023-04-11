package webtoon.account.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import webtoon.account.entities.UserEntity;
import webtoon.account.enums.EAccountType;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsernameAndIdNotLike(String username, Long id);

    boolean existsByEmailAndIdNotLike(String email, Long id);

    Page<UserEntity> findAllByStatus(Boolean status, Pageable pageable);

    Optional<UserEntity> findByAccountTypeAndUsername(EAccountType type, String username);

    Optional<UserEntity> findByUsername(String username);

}
