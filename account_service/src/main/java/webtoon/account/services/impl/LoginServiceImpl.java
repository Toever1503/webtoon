package webtoon.account.services.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import webtoon.account.entities.EAuthorityConstants;
import webtoon.account.entities.AuthorityEntity;
import webtoon.account.entities.UserEntity;
import webtoon.account.enums.EAccountType;
import webtoon.account.enums.EStatus;
import webtoon.account.models.LoginModel;
import webtoon.account.repositories.IAuthorityRepository;
import webtoon.account.repositories.IUserRepository;
import webtoon.account.services.LoginService;
import webtoon.utils.exception.CustomHandleException;

import javax.transaction.Transactional;
import java.util.List;


@Service
public class LoginServiceImpl implements LoginService {

    private final PasswordEncoder passwordEncoder;

    private final IUserRepository userRepository;

    private final IAuthorityRepository authorityRepository;

    public LoginServiceImpl(PasswordEncoder passwordEncoder, IUserRepository userRepository, IAuthorityRepository authorityRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
    }


    @Override
    public void login(LoginModel model) {
        UserEntity userEntity = this.userRepository
                .findByUsernameOrEmail(model.getUsername(), model.getUsername())
                .orElseThrow(
                        () -> new CustomHandleException(0)
                );

        if (!this.passwordEncoder.matches(model.getPassword(), userEntity.getPassword())) {
            throw new CustomHandleException(1);
        }
    }


    @Transactional
    @Override
    public void loginFormOAuth2(OAuth2AuthenticationToken token) {
        EAccountType type = EAccountType.valueOf(token.getAuthorizedClientRegistrationId().toUpperCase());

        OAuth2User oAuth2User = token.getPrincipal();
//
//        UserEntity userEntity = this.userRepository
//                .findByAccountTypeAndUsername(
//                        type,
//                        oAuth2User.getName()
//                )
//                .orElseGet(() -> this.userRepository.save(
//                                UserEntity.builder()
//                                        .username(oAuth2User.getName())
//                                        .fullName(oAuth2User.getAttribute("given_name"))
//                                        .avatar(oAuth2User.getAttribute("picture"))
//                                        .email(oAuth2User.getAttribute("email"))
//                                        .accountType(type)
//                                        .status(EStatus.ENABLED)
//                                        .build()
//                        )
//                );

    }

    @Override
    public int loginViaOAuth2(OAuth2AuthenticationToken token) {
        EAccountType accountType = EAccountType.valueOf(token.getAuthorizedClientRegistrationId().toUpperCase());
        OAuth2User oAuth2User = token.getPrincipal();

        String username;
        if (accountType == EAccountType.GOOGLE)
            username = accountType + "-" + oAuth2User.getAttribute("sub");
        else username = accountType + "-" + oAuth2User.getAttribute("id");

        UserEntity entity = this.userRepository.findByUsername(username)
                .orElse(null);

        int typeOfLogin = 0;
        if (entity == null) { // handle new user
            entity = UserEntity.builder()
                    .username(username)
                    .accountType(accountType)
                    .email(oAuth2User.getAttribute("email"))
                    .fullName(oAuth2User.getAttribute("name"))
                    .hasBlocked(false)
                    .numberOfFailedSignIn(1)
                    .status(EStatus.NOT_ENABLED)
                    .password(this.passwordEncoder.encode(username))
                    .build();
            if (accountType == EAccountType.GOOGLE)
                entity.setAvatar(oAuth2User.getAttribute("picture"));
            this.userRepository.saveAndFlush(entity);
            typeOfLogin = 0;
        } else { // handle old user
        }


        return typeOfLogin;
    }


}
