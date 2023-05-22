package webtoon.main.account.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import webtoon.main.account.models.ChangePasswordModel;
import webtoon.main.account.services.IForgotPasswordService;
import webtoon.main.account.services.IUserService;

import javax.mail.MessagingException;

@Controller
public class ResetPasswordController {

    @Autowired
    private IForgotPasswordService forgotPasswordService;

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "account/forgot_password";
    }


    @PostMapping("/forgot-password")
    public String processForgotPasswordForm(@RequestParam("email") String email, Model model) throws MessagingException {
        forgotPasswordService.sendResetPasswordEmail(email);
        model.addAttribute("message", "Đường dẫn thay đổi mật khẩu đã được gửi tới email của bạn.Vui lòng kiểm tra!.");
        return "account/forgot_password";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam(name = "q", required = false, defaultValue = "") String token, Model model) {
        if (token == null || token.isBlank())
            return "redirect:/signin";
        model.addAttribute("token", token);
        return "account/reset_password";
    }

    @PostMapping("/reset-password")
    public String processResetPasswordForm(@RequestParam("q") String token,
                                           ChangePasswordModel input,
                                           Model model) {
        try {
            if (!input.getNewPassword().equals(input.getConfirmPassword())) {
                model.addAttribute("error", "Mật khẩu không khớp.");
            } else
                forgotPasswordService.resetPassword(token, input);
            model.addAttribute("success", "Đặt lại mật khẩu thành công.");
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Đặt lại mật khẩu thất bại. Thử lại sau!");
        }
        return "account/reset_password";
    }
}
