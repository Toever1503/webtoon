package webtoon.main.account.services;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import webtoon.main.account.models.LoginModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ILoginService {
    void login(LoginModel model, HttpServletRequest req);

    int loginViaOAuth2(OAuth2AuthenticationToken token, HttpServletRequest req, HttpServletResponse res);
}
