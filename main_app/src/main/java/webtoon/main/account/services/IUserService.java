package webtoon.main.account.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import webtoon.main.account.configs.security.jwt.IJwtService;
import webtoon.main.account.dtos.LoginResponseDto;
import webtoon.main.account.dtos.UserDto;
import webtoon.main.account.entities.AuthorityEntity;
import webtoon.main.account.entities.UserEntity;
import webtoon.main.account.enums.EStatus;
import webtoon.main.account.inputs.LoginInput;
import webtoon.main.account.inputs.UserInput;

import java.util.List;
import java.util.Optional;

public interface IUserService extends IJwtService {


    Page<UserDto> findAllByStatus(Boolean status, Pageable pageable);

    UserDto findById(Long id);

    void delete(Long id);


    void checkHasBlockedAndUpdateNumberOfFailedSignInAndUpdateHasBlockedByNumberOfFailedSignIn(String username);


    List<AuthorityEntity> getAllAuthorities();

    UserDto add(UserInput input);
    UserDto update(UserInput input);

    void changeStatus(Long id, EStatus status);

    void unHasBlockedByNumberOfFailedSignInAndResetNumberOfFailedSignIn(Long id);

    Page<UserDto> findAll(Pageable pageable, Specification<UserEntity> finalSpec);
    Page<UserEntity> findAllEntities(Pageable pageable, Specification<UserEntity> finalSpec);

    void forgotPassword(String email);

    LoginResponseDto signin(LoginInput input);

    UserEntity getById(Long userId);

    Long countTotalRegisterTrialThisMonth();

    void saveUserEntity(UserEntity entity);

    UserEntity changePassword(Long id, String newPassword);

    Optional<UserEntity> findByPhone(String phone);

}
