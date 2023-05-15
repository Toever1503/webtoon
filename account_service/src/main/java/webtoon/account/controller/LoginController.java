package webtoon.account.controller;

import lombok.RequiredArgsConstructor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
    public String loginForm(Model model, @RequestParam(required = false, defaultValue = "") String redirectTo, HttpSession session) {
        model.addAttribute("loginModel", new LoginModel());
        session.setAttribute("redirectTo", redirectTo.isEmpty() ? null : redirectTo);
        return "account/login-form";
    }

    @ResponseBody
    @PostMapping("signin")
    public String loginHandle(LoginModel model, HttpSession session, HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            this.loginService.login(model, req);

            String redirectTo = (String) session.getAttribute("redirectTo");
            if (session != null)
                session.removeAttribute("redirectTo");
            res.sendRedirect("/" + (redirectTo.isEmpty() ? "" : "/" + redirectTo));
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
                                  HttpServletResponse res,
                                  HttpSession session) throws IOException {
        if (token == null)
            return "redirect:/oauth2-failed";
        else {
            int result = this.loginService.loginViaOAuth2(token, req, res);
            String redirectTo = (String) session.getAttribute("redirectTo");
            if (session != null)
                session.removeAttribute("redirectTo");

            if (redirectTo != null)
                return "redirect:" + redirectTo + "?login-type=" + result;
            else
                return "redirect:/index?login-type=" + result;
        }

    }

    @RequestMapping(value = "oauth2-failed")
    public String onOauth2Failed(Model model) {
        model.addAttribute("message", "Đăng nhập thất bại. Vui lòng thử lại sau!");
        return "account/login-form";
    }

    @RequestMapping("signout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
