package webtoon.account.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import webtoon.account.dtos.UserDto;
import webtoon.account.entities.UserEntity;
import webtoon.account.enums.EAccountType;
import webtoon.account.enums.ESex;
import webtoon.account.models.CreateUserModel;
import webtoon.account.models.UpdateUserModel;
import webtoon.account.repositories.IUserRepository;
import webtoon.account.services.UserService;
import webtoon.utils.exception.CustomHandleException;

import javax.transaction.Transactional;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;

    private final IUserRepository userRepository;

    @Override
    public Page<UserDto> findAllByStatus(Boolean status, Pageable pageable) {
        return this.userRepository.findAllByStatus(status, pageable)
                .map(UserDto::toDto);
    }

    @Override
    public UserDto findById(Long id) {
        UserEntity entity = this.userRepository.findById(id)
                .orElseThrow(
                        () -> new CustomHandleException(0)
                );
        return UserDto.toDto(entity);
    }

    @Transactional
    @Override
    public UserDto add(CreateUserModel model) {
        if (!Objects.equals(model.getPassword(), model.getRePassword())) {
            throw new CustomHandleException(2);
        }

        if (this.userRepository.existsByUsername(model.getUsername()))
            throw new CustomHandleException(1);
        if (this.userRepository.existsByEmail(model.getEmail()))
            throw new CustomHandleException(2);

        return UserDto.toDto(
                this.userRepository.save(
                        UserEntity.builder()
                                .username(model.getUsername())
                                .password(this.passwordEncoder.encode(model.getPassword()))
                                .fullName(model.getFullName())
                                .email(model.getEmail())
                                .accountType(EAccountType.DATABASE)
                                .sex(model.getSex())
//                                .status(EnumStatus.NOT_ACTIVE)
                                .build()
                )
        );
    }

    @Transactional
    @Override
    public UserDto update(UpdateUserModel model) {
        UserEntity entity = this.userRepository.findById(model.getId())
                .orElseThrow(
                        () -> new CustomHandleException(0)
                );

        if (this.userRepository.existsByEmailAndIdNotLike(model.getEmail(), model.getId())) {
            throw new CustomHandleException(2);
        }

        entity.setEmail(model.getEmail());
        entity.setFullName(model.getFullName());
        entity.setSex(ESex.valueOf(model.getSex()));

        return UserDto.toDto(this.userRepository.saveAndFlush(entity));
    }

    @Override
    public void delete(Long id) {
        this.userRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void changeStatus(Long id) {
        UserEntity entity = this.userRepository.findById(id)
                .orElseThrow(
                        () -> new CustomHandleException(0)
                );

//        entity.setStatus(!entity.getStatus());

        this.userRepository.saveAndFlush(entity);
    }

    @Transactional
    @Override
    public void checkHasBlockedAndUpdateNumberOfFailedSignInAndUpdateHasBlockedByNumberOfFailedSignIn(String username) {
        UserEntity entity = this.findByUsername(username);

        if (entity.getHasBlocked())
            throw new CustomHandleException(2);

        entity.setNumberOfFailedSignIn(entity.getNumberOfFailedSignIn() + 1);
        if (entity.getNumberOfFailedSignIn() >= 5)
            entity.setHasBlocked(Boolean.TRUE);

        this.userRepository.saveAndFlush(entity);
    }

    @Transactional
    @Override
    public void unHasBlockedByNumberOfFailedSignInAndResetNumberOfFailedSignIn(String username) {
        UserEntity entity = this.findByUsername(username);

        entity.setHasBlocked(Boolean.TRUE);
        entity.setNumberOfFailedSignIn(0);

        this.userRepository.saveAndFlush(entity);
    }

    private UserEntity findByUsername(String username) {
        return this.userRepository.findByAccountTypeAndUsername(
                        EAccountType.DATABASE,
                        username
                )
                .orElseThrow(
                        () -> new CustomHandleException(0)
                );
    }

}
