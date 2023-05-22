package webtoon.main.account.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import webtoon.main.account.configs.security.CustomUserDetail;
import webtoon.main.account.configs.security.jwt.JwtProvider;
import webtoon.main.account.dtos.LoginResponseDto;
import webtoon.main.account.dtos.UserDto;
import webtoon.main.account.entities.*;
import webtoon.main.account.enums.EAccountType;
import webtoon.main.account.enums.ESex;
import webtoon.main.account.enums.EStatus;
import webtoon.main.account.inputs.LoginInput;
import webtoon.main.account.inputs.UserInput;
import webtoon.main.account.models.CreateUserModel;
import webtoon.main.account.repositories.IAuthorityRepository;
import webtoon.main.account.repositories.IRoleRepository;
import webtoon.main.account.repositories.IUserRepository;
import webtoon.main.account.services.IUserService;
import webtoon.main.utils.exception.CustomHandleException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@org.springframework.transaction.annotation.Transactional
public class UserServiceImpl implements IUserService {
    private final PasswordEncoder passwordEncoder;

    private final IUserRepository userRepository;

    private final IAuthorityRepository authorityRepository;
    private final JwtProvider jwtProvider;

    private final IRoleRepository roleRepository;

    public UserServiceImpl(PasswordEncoder passwordEncoder, IUserRepository userRepository, IAuthorityRepository authorityRepository, JwtProvider jwtProvider, IRoleRepository roleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.jwtProvider = jwtProvider;
        this.roleRepository = roleRepository;

        initAuthority();

        if ((userRepository.findById(1l).isEmpty())) {
            UserEntity userEntity = UserEntity.builder()
                    .id(1l)
                    .email("admin@admin.com")
                    .phone("0123456789")
                    .username("admin")
                    .password(passwordEncoder.encode("123456"))
                    .status(EStatus.ACTIVED)
                    .sex(ESex.FEMALE)
                    .accountType(EAccountType.DATABASE)
                    .hasBlocked(false)
                    .fullName("Admin")
                    .role(roleRepository.findById(1l).get())
                    .avatar("")
                    .build();
            this.userRepository.saveAndFlush(userEntity);
        }

//        UserEntity userEntity = this.userRepository.findById(3l).get();
//        userEntity.setPassword(this.passwordEncoder.encode("1234567"));
//        this.userRepository.saveAndFlush(userEntity);

    }


    private void initAuthority() {
        // for manga
        authorityRepository.saveAndFlush(
                AuthorityEntity.builder()
                        .id(1L)
                        .authorityName(EAuthorityConstants.ROLE_MANAGE_MANGA)
                        .build()
        );

        // for order
        authorityRepository.saveAndFlush(
                AuthorityEntity.builder()
                        .id(2L)
                        .authorityName(EAuthorityConstants.ROLE_MANAGE_ORDER)
                        .build()
        );
        authorityRepository.saveAndFlush(
                AuthorityEntity.builder()
                        .id(3L)
                        .authorityName(EAuthorityConstants.ROLE_DELETE_ORDER)
                        .build()
        );

        authorityRepository.saveAndFlush(
                AuthorityEntity.builder()
                        .id(4L)
                        .authorityName(EAuthorityConstants.ROLE_VIEW_STATISTIC)
                        .build()
        );


        this.roleRepository.saveAndFlush(
                RoleEntity.builder()
                        .id(1l)
                        .roleName(ERoleConstants.ADMIN)
                        .build()
        );
        this.roleRepository.saveAndFlush(
                RoleEntity.builder()
                        .id(2l)
                        .roleName(ERoleConstants.EMP)
                        .build()
        );
        this.roleRepository.saveAndFlush(
                RoleEntity.builder()
                        .id(3l)
                        .roleName(ERoleConstants.CUS)
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
    public UserEntity findByEmail(String email){
        return this.userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new CustomHandleException(0)
                );
    }
    @Override
    public UserEntity findByUserName(String name){
        return  this.userRepository.findByUsername(name)
                .orElseThrow(
                        () -> new CustomHandleException(0)
                );

    }

    @Override
    public UserDto add(UserInput input) {
        UserEntity entity = UserInput.toEntity(input);

        if (this.userRepository.findByUsername(input.getUsername()).isPresent())
            throw new CustomHandleException(11);

        if (this.userRepository.existsByEmail(input.getEmail())) {
            throw new CustomHandleException(12);
        }
        entity.setHasBlocked(false);
        entity.setStatus(EStatus.ACTIVED);
        entity.setAccountType(EAccountType.DATABASE);
        entity.setPassword(this.passwordEncoder.encode(input.getPassword()));

        if (input.getRole() != null) {
            entity.setRole(this.roleRepository.findById(input.getRole()).get());
        }
        if (input.getAuthorities() != null)
            entity.setAuthorities(this.authorityRepository.findAllById(input.getAuthorities()).stream().collect(Collectors.toSet()));
        this.userRepository.saveAndFlush(entity);
        return UserDto.toDto(entity);
    }

    @Override
    public UserDto addDK(CreateUserModel input, BindingResult bindingResult) {
        UserEntity entity = new UserEntity();

        List<String> errors = new ArrayList<>();

        if (userRepository.existsByUsername(input.getUsername())){
            errors.add("UserName đã được sử dụng!");
        }

        // Kiểm tra xem email đã được sử dụng chưa
        if (userRepository.existsByEmail(input.getEmail())) {
            errors.add("Email này đã được sử dụng để đăng kí!");
        }

        // Kiểm tra xem số điện thoại đã được sử dụng chưa
        if (userRepository.existsByPhone(input.getPhone())) {
            errors.add("Số điện thoại này đã được sử dụng để đăng kí!");
        }

        // Kiểm tra xem password và confirm password có khớp nhau hay không
        if (!input.getPassword().equals(input.getRePassword())) {
            errors.add("Mật khẩu nhập lại không khớp!");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors.toString());
        }

        entity.setPassword(this.passwordEncoder.encode(input.getPassword()));
        entity.setPhone(input.getPhone());
        entity.setUsername(input.getUsername());
        entity.setFullName(input.getFullName());
        entity.setEmail(input.getEmail());
        entity.setStatus(EStatus.ACTIVED);
        entity.setAccountType(EAccountType.DATABASE);
        entity.setRole(this.roleRepository.findById(3l).get());
        this.userRepository.saveAndFlush(entity);
        return UserDto.toDto(entity);
    }


    @Override
    public void validateUser(CreateUserModel createUserModel, List<String> errors){
        if (userRepository.existsByUsername(createUserModel.getUsername())){
            errors.add("UserName đã được sử dụng!");
        }
        if (userRepository.existsByEmail(createUserModel.getEmail())) {
            errors.add("Email đã được sử dụng!");
        }
        if (userRepository.existsByPhone(createUserModel.getPhone())) {
            errors.add("Số điện thoại đã được sử dụng!");
        }
        if (!createUserModel.getPassword().equals(createUserModel.getRePassword())) {
            errors.add("Password không khớp nhau!");
        }
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
        entity.setEmail(input.getEmail().trim().toUpperCase());
        entity.setPhone(input.getPhone().trim().toUpperCase());
        entity.setSex(input.getSex());

        if (input.getRole() != null) {
            entity.setRole(this.roleRepository.findById(input.getRole()).get());
        }
        if (input.getAuthorities() != null)
            entity.setAuthorities(this.authorityRepository.findAllById(input.getAuthorities()).stream().collect(Collectors.toSet()));
        if (input.getPassword() != null)
            entity.setPassword(this.passwordEncoder.encode(input.getPassword().trim().toUpperCase()));

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
    public Long countTotalRegisterTrialThisMonth() {
        Specification spec = Specification
                .where((root, query, criteriaBuilder) -> criteriaBuilder.isNull(root.get(UserEntity_.TRIAL_REGISTERED_DATE)).not())
                .and((root, query, criteriaBuilder) -> criteriaBuilder.isNull(root.get(UserEntity_.DELETED_AT)));
        return this.userRepository.count(spec);
    }

    @Override
    public void saveUserEntity(UserEntity entity) {
        this.userRepository.saveAndFlush(entity);
    }

    @Override
    public UserEntity changePassword(Long id, String newPassword) {
        UserEntity userEntity = this.getById(id);
        userEntity.setPassword(this.passwordEncoder.encode(newPassword.trim()));
        this.userRepository.saveAndFlush(userEntity);
        return userEntity;
    }

    @Override
    public Optional<UserEntity> findByPhone(String phone) {
        return this.userRepository.findByPhone(phone);
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


        this.userRepository.saveAndFlush(entity);
    }

    @Override
    public void unHasBlockedByNumberOfFailedSignInAndResetNumberOfFailedSignIn(Long id) {
        UserEntity entity = this.getById(id);

        entity.setHasBlocked(Boolean.FALSE);
        this.userRepository.saveAndFlush(entity);
    }

    @Override
    public Page<UserDto> findAll(Pageable pageable, Specification<UserEntity> finalSpec) {
        return this.userRepository.findAll(finalSpec, pageable).map(UserDto::toDto);
    }

    @Override
    public Page<UserEntity> findAllEntities(Pageable pageable, Specification<UserEntity> finalSpec) {
        return this.userRepository.findAll(finalSpec, pageable);
    }

    @Override
    public void forgotPassword(String email) {

        UserEntity userEntity = this.findByUsername(email);
        // task: need send mail
    }

    @Override
    public LoginResponseDto signin(LoginInput input) {
        String email = input.getUsername();
        UserEntity userEntity = this.findByUsername(email);

        if (!BCrypt.checkpw(input.getPassword(), userEntity.getPassword())) {
            throw new CustomHandleException(2);
        }

        long validTimeIn = input.isRememberMe() ? 86400 : 3600;
        String token = this.jwtProvider.generateToken(
                userEntity.getUsername(),
                validTimeIn
        );

        List<String> auths = new ArrayList<>();
        if (userEntity.getAuthorities() != null) {
            for (AuthorityEntity authorityEntity : userEntity.getAuthorities()) {
                auths.add(authorityEntity.getAuthorityName().name());
            }
        }
        if (userEntity.getRole() != null) {
            auths.add(userEntity.getRole().getRoleName().name());
        }
        return LoginResponseDto.builder()
                .token(token)
                .validTimeIn(validTimeIn)
                .auths(auths)
                .fullName(userEntity.getFullName())
                .build();
    }

    @Override
    public List<AuthorityEntity> getAllAuthorities() {
        return this.authorityRepository.findAll();
    }

    private UserEntity findByUsername(String username) {
        return this.userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new CustomHandleException(22));
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
