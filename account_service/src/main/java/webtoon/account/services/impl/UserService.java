package webtoon.account.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.Validator;
import webtoon.account.dtos.UserDto;
import webtoon.account.entities.UserEntity;
import webtoon.account.models.UserModel;
import webtoon.account.repositories.IUserRepository;
import webtoon.account.services.IUserService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final IUserRepository userRepository;

    @Override
    public UserDto add(UserModel model) {
        return UserDto.toDto(
                this.userRepository.save(
                        UserEntity.builder()
                                .username(model.getUsername())
                                .fullName(model.getFullName())
                                .email(model.getEmail())
                                .sex(model.getSex())
                                .build()
                )
        );
    }

    @Override
    public UserDto update(UserModel model) {
        return null;
    }

    @Override
    public void delete() {

    }

}
