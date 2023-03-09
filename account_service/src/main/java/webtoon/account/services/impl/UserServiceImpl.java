package webtoon.account.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import webtoon.account.dtos.UserDto;
import webtoon.account.entities.UserEntity;
import webtoon.account.enums.EnumSex;
import webtoon.account.models.CreateUserModel;
import webtoon.account.models.UpdateUserModel;
import webtoon.account.repositories.IUserRepository;
import webtoon.account.services.UserService;
import webtoon.utils.exception.CustomHandleException;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final IUserRepository userRepository;

    @Override
    public Page<UserDto> findAllByStatus(Boolean status, Pageable pageable) {
        return this.userRepository.findAllByStatus(status, pageable)
                .map(UserDto::toDto);
    }

    public UserDto findById(Long id) {
        UserEntity entity = this.userRepository.findById(id)
                .orElseThrow(
                        () -> new CustomHandleException(1)
                );
        return UserDto.toDto(entity);
    }

    @Override
    public UserDto add(CreateUserModel model) {
        if (this.userRepository.existsByUsername(model.getUsername()))
            throw new CustomHandleException(1);
        if (this.userRepository.existsByEmail(model.getEmail()))
            throw new CustomHandleException(1);

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
    public UserDto update(UpdateUserModel model) {
        UserEntity entity = this.userRepository.findById(model.getId())
                .orElseThrow(
                        () -> new CustomHandleException(0)
                );

        if (this.userRepository.existsByEmailAndIdNotLike(model.getEmail(), model.getId()))
            throw new CustomHandleException(2);

        entity.setEmail(model.getEmail());
        entity.setFullName(model.getFullName());
        entity.setSex(EnumSex.valueOf(model.getSex()));

        return UserDto.toDto(this.userRepository.saveAndFlush(entity));
    }

    @Override
    public void delete(Long id) {
        this.userRepository.deleteById(id);
    }

    @Override
    public void changeStatus(Long id) {
        UserEntity entity = this.userRepository.findById(id)
                .orElseThrow(
                        () -> new CustomHandleException(0)
                );

//        entity.setStatus(!entity.getStatus());

        this.userRepository.saveAndFlush(entity);
    }

    @Override
    public void checkHasBlockedAndUpdateNumberOfFailedSignInAndUpdateHasBlockedByNumberOfFailedSignIn(String username) {
        UserEntity entity = this.findByUsernameAndStatusIsTrue(username);

        if (entity.getHasBlocked())
            throw new CustomHandleException(2);

        entity.setNumberOfFailedSignIn(entity.getNumberOfFailedSignIn() + 1);
        if (entity.getNumberOfFailedSignIn() >= 5)
            entity.setHasBlocked(Boolean.TRUE);

        this.userRepository.saveAndFlush(entity);
    }

    @Override
    public void unHasBlockedByNumberOfFailedSignInAndResetNumberOfFailedSignIn(String username) {
        UserEntity entity = this.findByUsernameAndStatusIsTrue(username);

        entity.setHasBlocked(Boolean.TRUE);
        entity.setNumberOfFailedSignIn(0);

        this.userRepository.saveAndFlush(entity);
    }

    private UserEntity findByUsernameAndStatusIsTrue(String username) {
        return this.userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new CustomHandleException(0)
                );
    }

}
