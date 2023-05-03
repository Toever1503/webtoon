package webtoon.account.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import webtoon.account.models.CreateUserModel;
import webtoon.account.services.IUserService;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/signup")
@RequiredArgsConstructor
public class RegisterUserController {

    private final IUserService userService;

    @GetMapping
    public String index(Model model, @RequestParam(required = false, defaultValue = "") String redirectTo, HttpSession session) {
        model.addAttribute("createModel",new CreateUserModel());
        session.setAttribute("redirectTo", redirectTo.isEmpty() ? null : redirectTo);
        return "account/register";
    }

    @PostMapping
    public String add(@ModelAttribute("createModel") CreateUserModel model) {
//        this.userService.add(model);
        return "login";
    }
}
