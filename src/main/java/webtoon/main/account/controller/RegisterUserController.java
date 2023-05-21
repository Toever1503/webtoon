package webtoon.main.account.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import webtoon.main.account.dtos.UserDto;
import webtoon.main.account.models.CreateUserModel;
import webtoon.main.account.services.IUserService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/signup")
@RequiredArgsConstructor
public class RegisterUserController {

    private final IUserService userService;

    @GetMapping
    public String index(Model model, @RequestParam(required = false, defaultValue = "") String redirectTo, HttpSession session) {
        model.addAttribute("createUserModel",new CreateUserModel());
        session.setAttribute("redirectTo", redirectTo.isEmpty() ? null : redirectTo);
        return "account/register";
    }

    @PostMapping
    public String signup(@ModelAttribute("createUserModel")  @Valid CreateUserModel createModel, Model model,BindingResult result) {
        List<String> err = new ArrayList<>();
        this.userService.validateUser(createModel,err);
        if (!err.isEmpty()) {
            for (String error : err) {
                result.addError(new ObjectError("createUserModel", error));
            }

            return "account/register";
        }
        if (result.hasErrors()) {
            return "account/register";
        }
        this.userService.addDK(createModel,result);
//        model.addAttribute("success", "Đăng ký thành công!");
        return "redirect:/login";
    }

}
