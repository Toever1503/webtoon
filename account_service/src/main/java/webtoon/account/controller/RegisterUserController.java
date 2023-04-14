package webtoon.account.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import webtoon.account.models.CreateUserModel;
import webtoon.account.services.IUserService;

@Controller
@RequestMapping(value = "/signup")
@RequiredArgsConstructor
public class RegisterUserController {

    private final IUserService userService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("createModel",new CreateUserModel());
        return "register";
    }

    @PostMapping
    public String add(@ModelAttribute("createModel") CreateUserModel model) {
//        this.userService.add(model);
        return "login";
    }
}
