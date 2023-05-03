package webtoon.account.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import webtoon.account.configs.security.SecurityUtils;
import webtoon.account.entities.UserEntity;
import webtoon.account.inputs.UserInput;
import webtoon.account.services.IUserService;

import javax.servlet.http.HttpSession;
import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService userService;


    @GetMapping("/profile")
    public String profile( Model model){
        UserEntity user = userService.getById(SecurityUtils.getCurrentUser().getUser().getId());
        model.addAttribute("user", user);
        return "account/profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute("user") UserInput user, BindingResult result){
        UserEntity userEntity = SecurityUtils.getCurrentUser().getUser();
        if(result.hasErrors()){
            System.out.println("Error");
            return "account/profile";
        }
        user.setId(userEntity.getId());
        userService.update(user);
        System.out.println("Success");
        return "redirect:/user/profile";
    }
}
