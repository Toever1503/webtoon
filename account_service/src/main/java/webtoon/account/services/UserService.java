package webtoon.account.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import webtoon.account.dtos.UserDto;
import webtoon.account.models.CreateUserModel;
import webtoon.account.models.UpdateUserModel;

public interface UserService {


    Page<UserDto> findAllByStatus(Boolean status, Pageable pageable);

    UserDto findById(Long id);

    UserDto add(CreateUserModel model);

    UserDto update(UpdateUserModel model);

    void delete(Long id);

    void changeStatus(Long id);

    void checkHasBlockedAndUpdateNumberOfFailedSignInAndUpdateHasBlockedByNumberOfFailedSignIn(String username);

    void unHasBlockedByNumberOfFailedSignInAndResetNumberOfFailedSignIn(String username);
}
