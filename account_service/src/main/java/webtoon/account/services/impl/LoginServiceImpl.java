package webtoon.account.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import webtoon.account.entities.FileEntity;
import webtoon.account.entities.UserEntity;
import webtoon.account.enums.EnumStatus;
import webtoon.account.repositories.IUserRepository;
import webtoon.account.services.LoginService;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final IUserRepository userRepository;

    @Override
    public void loginFormOAuth2(OAuth2AuthenticationToken token) {
        OAuth2User oAuth2User = token.getPrincipal();
        UserEntity userEntity =
                this.userRepository.findByUsername(oAuth2User.getName())
                        .orElse(
                                this.userRepository.save(
                                        UserEntity.builder()
                                                .username(oAuth2User.getName())
                                                .fullName(oAuth2User.getAttribute("given_name"))
                                                .avatar(oAuth2User.getAttribute("picture"))
                                                .email(oAuth2User.getAttribute("email"))
                                                .status(EnumStatus.ENABLED)
                                                .build()
                                )
                        );

    }
}
