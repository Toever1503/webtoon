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
import webtoon.payment.entities.OrderEntity;
import webtoon.payment.services.IOrderService;

import javax.servlet.http.HttpSession;
import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService userService;

    @Autowired
    private IOrderService orderService;

    @GetMapping("/profile")
    public String profile( Model model){
        UserEntity user = SecurityUtils.getCurrentUser().getUser();
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
        return "redirect:/";
    }

    @GetMapping("/userOrder")
    public String userOrder(Model model , HttpSession session) {
        UserEntity entity = (UserEntity) session.getAttribute("loggedUser");
        if(entity == null){
            return "redirect:/signin";
        }else {
            Long userId = SecurityUtils.getCurrentUser().getUser().getId();
            List<OrderEntity> order = orderService.getPaymentCompletedByUserId(userId);
            System.out.println("order: " + order);
            model.addAttribute("order", order);
            return "payments/userOrder";
        }
    }
}
