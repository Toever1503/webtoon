package webtoon.account.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import webtoon.account.models.CreateUserModel;
import webtoon.account.services.IUserService;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

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
    public String add(@ModelAttribute("createModel") CreateUserModel model,Model model1) {

        try {
            this.userService.addDk(model);
            return "account/login-form";
        }catch (Exception ex) {

            List<String> errorMessages = new ArrayList<>();
            Throwable throwable = ex;
            while (throwable != null) {
                errorMessages.add(throwable.getMessage());  // Thêm tất cả các thông báo lỗi vào danh sách
                throwable = throwable.getCause();
            }
            model1.addAttribute("errorMessages", errorMessages);  // Truyền danh sách thông báo lỗi qua Model để hiển thị trên giao diện
            ex.printStackTrace();
            return "account/register";
        }

    }
}
