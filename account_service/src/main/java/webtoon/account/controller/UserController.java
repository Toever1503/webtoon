package webtoon.account.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import webtoon.account.configs.security.SecurityUtils;
import webtoon.account.entities.UserEntity;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    @GetMapping("/profile/{id}")
    public String profile(Long id,Model model){
        UserEntity user = SecurityUtils.getCurrentUser().getUser();
        model.addAttribute("user", user);
        return "account/giaoDienNguoiDung";
    }
}
