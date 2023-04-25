package webtoon.account.controller;

import lombok.RequiredArgsConstructor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import webtoon.account.models.LoginModel;
import webtoon.account.services.ILoginService;
import webtoon.utils.exception.CustomHandleException;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final ILoginService loginService;


    @RequestMapping("/signin")
    public String loginForm(Model model) {
        model.addAttribute("loginModel", new LoginModel());
        return "account/login-form";
    }

    @ResponseBody
    @PostMapping("signin")
    public String loginHandle(LoginModel model, HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            this.loginService.login(model, req);
            res.sendRedirect("/");
        } catch (CustomHandleException e) {
            if (e.getCode() == 0) {
                return "login error";
            }
        }

        return null;
    }


    @RequestMapping(value = "oauth2-success")
    public String onOauth2Success(OAuth2AuthenticationToken token,
                                  HttpServletRequest req,
                                  HttpServletResponse res) throws IOException {
        if (token == null)
            return "redirect:/oauth2-failed";
        else {
            int result = this.loginService.loginViaOAuth2(token, req, res);
            return "redirect:/index?login-type=" + result;
        }

    }

    @RequestMapping(value = "oauth2-failed")
    public String onOauth2Failed(Model model) {
        model.addAttribute("message", "Đăng nhập thất bại. Vui lòng thử lại sau!");
        return "account/login-form";
    }
}
