package webtoon.payment.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import webtoon.account.configs.security.SecurityUtils;
import webtoon.account.entities.UserEntity;
import webtoon.payment.entities.OrderEntity;
import webtoon.payment.services.IOrderService;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/user")
public class OrderHistoryController {

    @Autowired
    private IOrderService orderService;

    @GetMapping("/userOrder")
    public String userOrder(Model model, HttpSession session) {
        UserEntity entity = (UserEntity) session.getAttribute("loggedUser");
        if (entity == null) {
            return "redirect:/signin";
        } else {
            Long userId = SecurityUtils.getCurrentUser().getUser().getId();
            UserEntity userEntity = SecurityUtils.getCurrentUser().getUser();
            List<OrderEntity> order = orderService.getPaymentCompletedByUserId(userId);
            System.out.println("order: " + order);
            model.addAttribute("user", userEntity);
            model.addAttribute("order", order);
            return "account/userOrder";
        }
    }
}
