package webtoon.account.controller;

import lombok.RequiredArgsConstructor;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import webtoon.account.models.LoginModel;
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
    public String form(@ModelAttribute("loginModel") LoginModel model, HttpServletResponse res) throws IOException {
        try{
            this.loginService.loginForm(model);
        }catch (CustomHandleException e){
            if (e.getCode() == 0){
                return "login error";
            }
        }
        res.sendRedirect("http://localhost:8000/");
		return null;
    }


    @GetMapping(value = "success")
    public void success(OAuth2AuthenticationToken token, HttpServletResponse res) throws IOException {
        this.loginService.loginFormOAuth2(token);
        res.sendRedirect("http://localhost:8000/");
    }

    @GetMapping(value = "error")
    public String error() {
        return "login";
    }
}
