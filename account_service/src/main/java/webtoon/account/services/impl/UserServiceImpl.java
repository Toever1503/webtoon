package webtoon.account.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import webtoon.account.configs.security.CustomUserDetail;
import webtoon.account.configs.security.jwt.JwtProvider;
import webtoon.account.enums.EAccountType;
import webtoon.account.enums.EStatus;
import webtoon.account.repositories.IUserRepository;
import webtoon.account.services.IUserService;
import webtoon.account.dtos.UserDto;
import webtoon.account.entities.AuthorityEntity;
import webtoon.account.entities.EAuthorityConstants;
import webtoon.account.entities.UserEntity;
import webtoon.account.inputs.UserInput;
import webtoon.account.repositories.IAuthorityRepository;
import webtoon.main.utils.exception.CustomHandleException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements IUserService {
    private final PasswordEncoder passwordEncoder;

    private final IUserRepository userRepository;

    private final IAuthorityRepository authorityRepository;
    private final JwtProvider jwtProvider;

    public UserServiceImpl(PasswordEncoder passwordEncoder, IUserRepository userRepository, IAuthorityRepository authorityRepository, JwtProvider jwtProvider) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.jwtProvider = jwtProvider;
        initAuthority();
    }


    private void initAuthority() {
        // for manga
        authorityRepository.saveAndFlush(
                AuthorityEntity.builder()
                        .id(1L)
                        .authorityName(EAuthorityConstants.ROLE_ADD_EDIT_MANGA)
                        .build()
        );
        authorityRepository.saveAndFlush(
                AuthorityEntity.builder()
                        .id(2L)
                        .authorityName(EAuthorityConstants.ROLE_DELETE_MANGA)
                        .build()
        );

        // for manga author
        authorityRepository.saveAndFlush(
                AuthorityEntity.builder()
                        .id(2L)
                        .authorityName(EAuthorityConstants.ROLE_ADD_EDIT_MANGA_AUTHOR)
                        .build()
        );
        authorityRepository.saveAndFlush(
                AuthorityEntity.builder()
                        .id(3L)
                        .authorityName(EAuthorityConstants.ROLE_DELETE_MANGA_AUTHOR)
                        .build()
        );

        // for manga genre
        authorityRepository.saveAndFlush(
                AuthorityEntity.builder()
                        .id(4L)
                        .authorityName(EAuthorityConstants.ROLE_ADD_EDIT_MANGA_GENRE)
                        .build()
        );
        authorityRepository.saveAndFlush(
                AuthorityEntity.builder()
                        .id(5L)
                        .authorityName(EAuthorityConstants.ROLE_DELETE_MANGA_GENRE)
                        .build()
        );

        // for user
        authorityRepository.saveAndFlush(
                AuthorityEntity.builder()
                        .id(6L)
                        .authorityName(EAuthorityConstants.ROLE_ADD_EDIT_USER)
                        .build()
        );
        authorityRepository.saveAndFlush(
                AuthorityEntity.builder()
                        .id(7L)
                        .authorityName(EAuthorityConstants.ROLE_DELETE_USER)
                        .build()
        );

        // for order
        authorityRepository.saveAndFlush(
                AuthorityEntity.builder()
                        .id(8L)
                        .authorityName(EAuthorityConstants.ROLE_ADD_EDIT_ORDER)
                        .build()
        );
        authorityRepository.saveAndFlush(
                AuthorityEntity.builder()
                        .id(9L)
                        .authorityName(EAuthorityConstants.ROLE_DELETE_ORDER)
                        .build()
        );

        // for category
        authorityRepository.saveAndFlush(
                AuthorityEntity.builder()
                        .id(10L)
                        .authorityName(EAuthorityConstants.ROLE_ADD_EDIT_CATEGORY)
                        .build()
        );
        authorityRepository.saveAndFlush(
                AuthorityEntity.builder()
                        .id(11L)
                        .authorityName(EAuthorityConstants.ROLE_DELETE_CATEGORY)
                        .build()
        );

        // for post
        authorityRepository.saveAndFlush(
                AuthorityEntity.builder()
                        .id(12L)
                        .authorityName(EAuthorityConstants.ROLE_ADD_EDIT_POST)
                        .build()
        );
        authorityRepository.saveAndFlush(
                AuthorityEntity.builder()
                        .id(13L)
                        .authorityName(EAuthorityConstants.ROLE_DELETE_POST)
                        .build()
        );

        // for comment
        authorityRepository.saveAndFlush(
                AuthorityEntity.builder()
                        .id(14L)
                        .authorityName(EAuthorityConstants.ROLE_ADD_EDIT_COMMENT)
                        .build()
        );
        authorityRepository.saveAndFlush(
                AuthorityEntity.builder()
                        .id(15L)
                        .authorityName(EAuthorityConstants.ROLE_DELETE_COMMENT)
                        .build()
        );

        // for tag
        authorityRepository.saveAndFlush(
                AuthorityEntity.builder()
                        .id(16L)
                        .authorityName(EAuthorityConstants.ROLE_ADD_EDIT_TAG)
                        .build()
        );
        authorityRepository.saveAndFlush(
                AuthorityEntity.builder()
                        .id(17L)
                        .authorityName(EAuthorityConstants.ROLE_DELETE_TAG)
                        .build()
        );

        // for statistic
        authorityRepository.saveAndFlush(
                AuthorityEntity.builder()
                        .id(18L)
                        .authorityName(EAuthorityConstants.ROLE_VIEW_STATISTIC)
                        .build()
        );

        // for role
        authorityRepository.saveAndFlush(
                AuthorityEntity.builder()
                        .id(19l)
                        .authorityName(EAuthorityConstants.ROLE_ADMIN)
                        .build()
        );

        authorityRepository.saveAndFlush(
                AuthorityEntity.builder()
                        .id(20l)
                        .authorityName(EAuthorityConstants.ROLE_USER)
                        .build()
        );

    }

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

    @Override
    public UserDto add(UserInput input) {
        UserEntity entity = UserInput.toEntity(input);

        if(this.userRepository.findByUsername(input.getUsername()).isPresent())
            throw new CustomHandleException(11);

        if (this.userRepository.existsByEmail(input.getEmail())) {
            throw new CustomHandleException(12);
        }
        entity.setHasBlocked(false);
        entity.setNumberOfFailedSignIn(0);
        entity.setStatus(EStatus.ACTIVED);
        entity.setAccountType(EAccountType.DATABASE);
        entity.setPassword(this.passwordEncoder.encode(input.getPassword()));
        entity.setAuthorities(this.authorityRepository.findAllById(input.getAuthorities()).stream().collect(Collectors.toSet()));
        this.userRepository.saveAndFlush(entity);
        return UserDto.toDto(entity);
    }

    @Transactional
    @Override
    public UserDto update(UserInput input) {
        UserEntity entity = this.userRepository.findById(input.getId())
                .orElseThrow(
                        () -> new CustomHandleException(0)
                );

        // check if email is existed
        if (this.userRepository.existsByEmailAndIdNotLike(input.getEmail(), input.getId())) {
            throw new CustomHandleException(12);
        }
        entity.setFullName(input.getFullName());
        entity.setEmail(input.getEmail());
        entity.setPhone(input.getPhone());
        entity.setSex(input.getSex());

        if (input.getAuthorities() != null)
            entity.setAuthorities(this.authorityRepository.findAllById(input.getAuthorities()).stream().collect(Collectors.toSet()));
        if (input.getPassword() != null)
            entity.setPassword(this.passwordEncoder.encode(input.getPassword()));

        return UserDto.toDto(this.userRepository.saveAndFlush(entity));
    }

    @Override
    public void delete(Long id) {
        try {
            this.userRepository.deleteById(id);
        } catch (Exception e) {
            throw new CustomHandleException(0);
        }
    }

    public UserEntity getById(Long id) {
        return this.userRepository.findById(id)
                .orElseThrow(
                        () -> new CustomHandleException(0)
                );
    }

    @Override
    public void changeStatus(Long id, EStatus status) {
        UserEntity entity = this.getById(id);
        entity.setStatus(status);
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

    @Override
    public void unHasBlockedByNumberOfFailedSignInAndResetNumberOfFailedSignIn(Long id) {
        UserEntity entity = this.getById(id);

        entity.setHasBlocked(Boolean.FALSE);
        entity.setNumberOfFailedSignIn(0);

        this.userRepository.saveAndFlush(entity);
    }

    @Override
    public Page<UserDto> findAll(Pageable pageable, Specification<UserEntity> finalSpec) {
        return this.userRepository.findAll(finalSpec, pageable).map(UserDto::toDto);
    }

    @Override
    public List<AuthorityEntity> getAllAuthorities() {
        return this.authorityRepository.findAll();
    }

    private UserEntity findByUsername(String username) {
        return null;
    }

    @Override
    public boolean tokenFilter(String token, HttpServletRequest req, HttpServletResponse res) {
        try {
            String username = this.jwtProvider.getUsernameFromToken(token);
            CustomUserDetail userDetail = new CustomUserDetail(this.findByUsername(username));
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());
            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}
