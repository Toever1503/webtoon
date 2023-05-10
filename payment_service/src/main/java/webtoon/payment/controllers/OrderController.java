package webtoon.payment.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import webtoon.account.configs.security.SecurityUtils;
import webtoon.account.entities.UserEntity;
import webtoon.payment.dtos.OrderPendingDTO;
import webtoon.payment.enums.EOrderStatus;
import webtoon.payment.enums.EOrderType;
import webtoon.payment.enums.EPaymentMethod;
import webtoon.payment.services.IOrderService;
import webtoon.payment.services.ISubscriptionPackService;
import webtoon.payment.entities.OrderEntity;
import webtoon.payment.entities.SubscriptionPackEntity;
import webtoon.payment.models.OrderModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private ISubscriptionPackService subscriptionPackService;
    @Autowired
    private IOrderService orderService;

//    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");

    public OrderController(IOrderService orderService) {
        super();
        this.orderService = orderService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addOrder(@RequestBody OrderModel orderModel) {
        return ResponseEntity.ok(orderService.add(orderModel));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateOrder(@RequestBody OrderModel orderModel) {
        return ResponseEntity.ok(orderService.update(orderModel));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<OrderEntity>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAll());
    }


    @GetMapping("create-order")
    public String createOrder(@RequestParam Long subscriptionPack, @RequestParam EPaymentMethod paymentMethod) {
        SubscriptionPackEntity subscriptionPackEntity = this.subscriptionPackService.getById(subscriptionPack);
        OrderEntity orderEntity = this.orderService.createDraftedOrder(subscriptionPackEntity, paymentMethod);

        if (paymentMethod.equals(EPaymentMethod.VN_PAY)) {
            return "redirect:/payment/pay?orderId=" + orderEntity.getId();
        }
        return "redirect:/order/bank-info/" + orderEntity.getId();
    }

    @GetMapping("/bank-info/{id}")
    public String getBankInfoForOrder(@PathVariable Long id, Model model, HttpSession session) {
        UserEntity entity = (UserEntity) session.getAttribute("loggedUser");
        if (!SecurityUtils.isAuthenticated()) {
            return "redirect:/signin";
        } else {
            OrderEntity orderEntity = this.orderService.getById(id);
            SubscriptionPackEntity subscriptionPackEntity = orderEntity.getSubs_pack_id();
            model.addAttribute("name", subscriptionPackEntity.getName());
            Double price = subscriptionPackEntity.getPrice();
            model.addAttribute("price", price);
            model.addAttribute("id", subscriptionPackEntity.getId());
            model.addAttribute("subscriptionPackId", subscriptionPackEntity.getId());
            model.addAttribute("maDonHang", orderEntity.getMaDonHang());
            return "payments/chuyenKhoan";
        }
    }

    @GetMapping("chuyenKhoan/confirmed")
    public String paypal(Model model, HttpSession session,
                         @RequestParam String maDonHang) {
        UserEntity entity = (UserEntity) session.getAttribute("loggedUser");
        if (entity == null) {
            return "redirect:/signin";
        } else {
            this.orderService.userConfirmOrder(maDonHang);
            return "payments/confirmed";
        }
    }


    @RequestMapping("cancelOrder/{id}")
    public String cancelOrder(@PathVariable Long id,
                              @RequestParam(defaultValue = "0") Integer page,
                              RedirectAttributes redirectAttributes) {
        this.orderService.cancelOrder(id);
        redirectAttributes.addAttribute("showNotification", true);
        redirectAttributes.addAttribute("notificationMessage", "Hủy thành công!");
        return "redirect:/user/userOrder" + (page == 0 ? "" : "?page=" + page);
    }

    @GetMapping("repay/{id}")
    public String repay(@PathVariable Long id, Model model) {
        OrderEntity orderEntity = this.orderService.getById(id);
        if (orderEntity.getPaymentMethod().equals(EPaymentMethod.VN_PAY))
            return "redirect:/payment/pay?orderId=" + orderEntity.getId();
        else
            return "redirect:/order/bank-info/" + orderEntity.getId();

    }


}
