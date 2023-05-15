package webtoon.account.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import webtoon.account.entities.UserEntity;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsernameAndIdNotLike(String username, Long id);

    boolean existsByEmailAndIdNotLike(String email, Long id);

    Page<UserEntity> findAllByStatus(Boolean status, Pageable pageable);

    Optional<UserEntity> findByUsernameOrEmail(String username, String email);

    Optional<UserEntity> findByUsername(String username);

    @Query("SELECT COUNT(u) FROM UserEntity u WHERE trim(u.email) = trim(?1) ")
    Long checkTrungEmail(String email);

    @Query("SELECT COUNT(u) FROM UserEntity u WHERE trim(u.phone) = trim(?1) ")
    Long checkTrungPhone(String phone);

}
