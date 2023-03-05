package webtoon.account.services;

import webtoon.account.dtos.UserDto;
import webtoon.account.models.UserModel;

import java.util.Optional;

public interface IUserService {

    UserDto add(UserModel model);

    UserDto update(UserModel model);

    void delete();
}
