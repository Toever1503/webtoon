package webtoon.account.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import webtoon.account.entities.UserEntity;
import webtoon.account.enums.EnumAccountType;
import webtoon.account.enums.EnumStatus;
import webtoon.account.models.LoginModel;
import webtoon.account.repositories.IUserRepository;
import webtoon.account.services.LoginService;
import webtoon.utils.exception.CustomHandleException;


@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final PasswordEncoder passwordEncoder;

    private final IUserRepository userRepository;

    @Override
    public void loginForm(LoginModel model) {
        UserEntity userEntity = this.userRepository
                .findByAccountTypeAndUsername(
                        EnumAccountType.DATABASE,
                        model.getUsername()
                )
                .orElseThrow(
                        () -> new CustomHandleException(0)
                );

        if (!this.passwordEncoder.matches(model.getPassword(), userEntity.getPassword())) {
            throw new CustomHandleException(1);
        }

        this.setAuthentication(
                User.withUsername(userEntity.getUsername())
                        .password(userEntity.getPassword())
                        .roles("GUEST")
                        .build()
        );
    }

    @Override
    public void loginFormOAuth2(OAuth2AuthenticationToken token) {
        EnumAccountType type = EnumAccountType.valueOf(token.getAuthorizedClientRegistrationId().toUpperCase());

        OAuth2User oAuth2User = token.getPrincipal();

        UserEntity userEntity = this.userRepository
                .findByAccountTypeAndUsername(
                        type,
                        oAuth2User.getName()
                )
                .orElseGet(() -> this.userRepository.save(
                                UserEntity.builder()
                                        .username(oAuth2User.getName())
                                        .fullName(oAuth2User.getAttribute("given_name"))
                                        .avatar(oAuth2User.getAttribute("picture"))
                                        .email(oAuth2User.getAttribute("email"))
                                        .accountType(type)
                                        .status(EnumStatus.ENABLED)
                                        .build()
                        )
                );

        this.setAuthentication(
                User.withUsername(userEntity.getUsername())
                        .password("")
                        .roles("GUEST")
                        .build()
        );
    }

    private void setAuthentication(UserDetails userDetails) {

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                )
        );
    }
}
