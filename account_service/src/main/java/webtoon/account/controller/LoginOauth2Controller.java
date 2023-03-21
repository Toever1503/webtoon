package webtoon.account.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import webtoon.account.enums.EnumAccountType;
import webtoon.account.models.LoginModel;
import webtoon.account.repositories.IUserRepository;
import webtoon.account.services.LoginService;
import webtoon.utils.exception.CustomHandleException;

@Controller
@RequestMapping(value = "login")
@RequiredArgsConstructor
public class LoginOauth2Controller {

    private final LoginService loginService;


    @GetMapping(value = "form")
    public String getForm(Model model) {
        model.addAttribute("loginModel",new LoginModel());
        return "login";
    }

    @ResponseBody
    @PostMapping
    public String form(@ModelAttribute("loginModel") LoginModel model) {
        try{
            this.loginService.loginForm(model);
        }catch (CustomHandleException e){
            if (e.getCode() == 0){
                return "login error";
            }
        }
        return "login success";
    }


    @ResponseBody
    @GetMapping(value = "success")
    public String success(OAuth2AuthenticationToken token) {
        this.loginService.loginFormOAuth2(token);
        return "login success";
    }

    @GetMapping(value = "error")
    public String error() {
        return "login";
    }
}
