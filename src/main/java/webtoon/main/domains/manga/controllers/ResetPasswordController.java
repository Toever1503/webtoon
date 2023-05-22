package webtoon.main.domains.manga.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import webtoon.main.account.services.IForgotPasswordService;

import javax.mail.MessagingException;

@Controller
@RequestMapping("/forgot-password")
public class ResetPasswordController {

    @Autowired
    private IForgotPasswordService forgotPasswordService;

    @GetMapping()
    public String showForgotPasswordForm() {
        return "account/forgot_password";
    }


    @PostMapping()
    public String processForgotPasswordForm(@RequestParam("email") String email, Model model) throws MessagingException {
        forgotPasswordService.sendResetPasswordEmail(email);
        model.addAttribute("message", "Reset password link sent to email.");
        return "account/forgot_password_success";
    }
}
