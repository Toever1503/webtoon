package webtoon.account.services;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

public interface LoginService {
    void loginFormOAuth2(OAuth2AuthenticationToken token);
}
