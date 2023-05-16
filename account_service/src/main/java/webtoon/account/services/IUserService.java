package webtoon.account.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;
import webtoon.account.configs.security.jwt.IJwtService;
import webtoon.account.dtos.LoginResponseDto;
import webtoon.account.dtos.UserDto;
import webtoon.account.entities.AuthorityEntity;
import webtoon.account.entities.UserEntity;
import webtoon.account.enums.EStatus;
import webtoon.account.inputs.LoginInput;
import webtoon.account.inputs.UserInput;

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
