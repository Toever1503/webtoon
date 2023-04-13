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
import webtoon.main.utils.exception.CustomHandleException;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;


    @RequestMapping("/signin")
    public String loginForm(Model model) {
        model.addAttribute("loginModel", new LoginModel());
        return "login-form";
    }

    @ResponseBody
    @PostMapping("signin")
    public String loginHandle(LoginModel model, HttpServletResponse res) throws IOException {
        try {
            this.loginService.login(model);
            res.sendRedirect("http://localhost:8000/");
        } catch (CustomHandleException e) {
            if (e.getCode() == 0) {
                return "login error";
            }
        }

        return null;
    }


    @RequestMapping(value = "oauth2-success")
    public void onOauth2Success(OAuth2AuthenticationToken token, HttpServletResponse res) throws IOException {
        if (token == null)
            res.sendRedirect("/oauth2-failed");
        else {
            int result = this.loginService.loginViaOAuth2(token);
            res.sendRedirect("http://localhost:8000/?login-type=" + result);
        }
    }

    @RequestMapping(value = "oauth2-failed")
    public String onOauth2Failed(Model model) {
        model.addAttribute("message", "Đăng nhập thất bại. Vui lòng thử lại sau!");
        return "login-form";
    }
}
