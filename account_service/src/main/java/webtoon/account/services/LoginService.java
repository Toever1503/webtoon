package webtoon.account.services;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import webtoon.account.entities.AuthorityEntity;
import webtoon.account.models.LoginModel;

import java.util.List;

public interface LoginService {
    void login(LoginModel model);


    void loginFormOAuth2(OAuth2AuthenticationToken token);

    int loginViaOAuth2(OAuth2AuthenticationToken token);
}
