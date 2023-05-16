package webtoon.main.account.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import webtoon.main.account.configs.security.SecurityUtils;
import webtoon.main.account.entities.UserEntity;
import webtoon.main.account.inputs.UserInput;
import webtoon.main.account.models.ChangePasswordModel;
import webtoon.main.account.models.UpdateUserInfo;
import webtoon.main.account.services.IUserService;

import javax.servlet.http.HttpSession;


@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/profile")
    public String profile(Model model) {
        if (!SecurityUtils.isAuthenticated()) {
            return "redirect:/signin?redirectTo=/user/profile";
        }
        UserEntity user = userService.getById(SecurityUtils.getCurrentUser().getUser().getId());
        model.addAttribute("user", user);
        return "account/profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute("user") UserInput user, BindingResult result){
        UserEntity userEntity = SecurityUtils.getCurrentUser().getUser();
        if (result.hasErrors()) {
            System.out.println("Error");
            return "account/profile";
        }
        user.setId(userEntity.getId());
        userService.update(user);
        System.out.println("Success");
        return "redirect:/user/profile";
    }


    @RequestMapping("change_password")
    public String changePassword(HttpSession session, Model model) {
        UserEntity loggedUser = (UserEntity) session.getAttribute("loggedUser");
        if (loggedUser == null)
            return "redirect:/signin?redirectTo=/user/change_password";
        return "account/change_password";
    }

    @PostMapping("change_password/handle")
    public String handleChangePassword(HttpSession session, Model model, ChangePasswordModel changePasswordModel) {
        UserEntity loggedUser = (UserEntity) session.getAttribute("loggedUser");
        if (loggedUser == null)
            return "redirect:/signin?redirectTo=/user/change_password";

        if (!BCrypt.checkpw(changePasswordModel.getOldPassword().trim(), loggedUser.getPassword())) {
            model.addAttribute("error_message", "Mật khẩu cũ không đúng!");
            return "account/change_password";
        }

        if (!changePasswordModel.getNewPassword().trim().equals(changePasswordModel.getConfirmPassword().trim())) {
            model.addAttribute("error_message", "Mật khẩu nhập lại không khớp!");
            return "account/change_password";
        }

        try {
            loggedUser = this.userService.changePassword(loggedUser.getId(), changePasswordModel.getNewPassword());
            session.setAttribute("loggedUser", loggedUser);
            model.addAttribute("success_message", "Đổi mật khẩu thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error_message", "Đổi mật khẩu thất bại!");
        }
        return "account/change_password";
    }

    @RequestMapping("update_more_info")
    public String updateMoreInfo(HttpSession session) {
        UserEntity loggedUser = (UserEntity) session.getAttribute("loggedUser");
        if (loggedUser == null)
            return "redirect:/signin?redirectTo=/user/update_more_info";
        return "account/update_more_info";
    }

    @PostMapping("update_more_info/handle")
    public String updateMoreInfo(UpdateUserInfo updateUserInfo, HttpSession session, Model model) {
        UserEntity loggedUser = (UserEntity) session.getAttribute("loggedUser");
        if (loggedUser == null)
            return "redirect:/signin?redirectTo=/user/update_more_info";

        if (this.userService.findByPhone(updateUserInfo.getPhone()).isPresent()) {
            model.addAttribute("error_message", "Số điện thoại đã được sử dụng!");
            return "account/update_more_info";
        }
        try {
            loggedUser.setPhone(updateUserInfo.getPhone());
            loggedUser.setSex(updateUserInfo.getSex());
            loggedUser.setPassword(this.passwordEncoder.encode(updateUserInfo.getPassword()));
            this.userService.saveUserEntity(loggedUser);
            session.setAttribute("loggedUser", loggedUser);
            model.addAttribute("notifySuccessMessage", "Cập nhật thông tin thành công!");
            return "redirect:/user/profile";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error_message", "Cập nhật thông tin thất bại!");
        }
        return "account/update_more_info";
    }

}
