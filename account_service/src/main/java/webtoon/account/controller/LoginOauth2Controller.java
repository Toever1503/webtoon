package webtoon.account.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import webtoon.account.services.LoginService;

@RestController
@RequestMapping(value = "/oauth2/login")
@RequiredArgsConstructor
public class LoginOauth2Controller {

    private final LoginService loginService;


    @GetMapping(value = "form")
    public String form() {
        return "oauth2/login";
    }

    @GetMapping(value = "success")
    public String success(OAuth2AuthenticationToken token) {
        this.loginService.loginFormOAuth2(token);
        return "home";
    }

    @GetMapping(value = "error")
    public String error() {
        return "oauth2/login";
    }
}
