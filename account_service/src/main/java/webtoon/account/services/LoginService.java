package webtoon.account.services;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import webtoon.account.models.LoginModel;

public interface LoginService {
    void loginForm(LoginModel model);
    void loginFormOAuth2(OAuth2AuthenticationToken token);
}
