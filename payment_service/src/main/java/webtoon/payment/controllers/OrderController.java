package webtoon.payment.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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

import javax.servlet.http.HttpSession;
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
    public ResponseEntity<?> updateOrder(@RequestBody OrderModel orderModel){
        return ResponseEntity.ok(orderService.update(orderModel));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<OrderEntity>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAll());
    }

    @GetMapping("/chuyenKhoan/{id}")
    public String chuyenKhoan(@PathVariable Long id, Model model , HttpSession session) {
//        SubscriptionPackEntity subscriptionPackEntity = this.subscriptionPackService.getByPrice(orderModel.getFinalPrice());
        UserEntity entity = (UserEntity) session.getAttribute("loggedUser");
        if(entity == null){
            return "redirect:/signin";
        }else {

            Date createDate = new Date();
            SubscriptionPackEntity subscriptionPackEntity = subscriptionPackService.getById(id);
//        System.out.println("price: "+subscriptionPackEntity.getPrice());
            model.addAttribute("name", subscriptionPackEntity.getName());
            Double price = subscriptionPackEntity.getPrice();
            model.addAttribute("price", price);
            model.addAttribute("id",subscriptionPackEntity.getId());
            return "payments/chuyenKhoan";
        }
    }
    @GetMapping("/confirmed/{id}")
    public String paypal(@PathVariable Long id, Model model , HttpSession session) {
//        SubscriptionPackEntity subscriptionPackEntity = this.subscriptionPackService.getByPrice(orderModel.getFinalPrice());
        UserEntity entity = (UserEntity) session.getAttribute("loggedUser");
        if(entity == null){
            return "redirect:/signin";
        }else {
            String maDonHang = VnPayConfig.getRandomNumber(8);
            Date createDate = new Date();
            SubscriptionPackEntity subscriptionPackEntity = subscriptionPackService.getById(id);
//        System.out.println("price: "+subscriptionPackEntity.getPrice());
            Double price = subscriptionPackEntity.getPrice();
            model.addAttribute("name", subscriptionPackEntity.getName());
            orderService.add(new OrderModel(
                    Long.parseLong(maDonHang),createDate,createDate,price,EOrderType.EXTEND,EOrderStatus.USER_CONFIRMED_BANKING,"CHUYENKHOAN","0.0.0.0.1",maDonHang,subscriptionPackEntity,entity,EPaymentMethod.ATM));
            return "payments/confirmed";
        }
    }

//    @GetMapping("/userOrder/{id}")
//    public String userOrder(@PathVariable Long id,Model model , HttpSession session) {
//        UserEntity entity = (UserEntity) session.getAttribute("loggedUser");
//        if(entity == null){
//            return "redirect:/signin";
//        }else {
//            Long userId = SecurityUtils.getCurrentUser().getUser().getId();
//            List<OrderEntity> order = orderService.getPaymentCompletedByUserId(userId);
//            System.out.println("order: " + order);
//            model.addAttribute("order", order);
//            return "payments/userOrder";
//        }
//    }

//    @GetMapping("/pending-payment/{id}")
//    public String pendingPayment(@PathVariable Long id,Model model, HttpSession session) {
//        UserEntity entity = (UserEntity) session.getAttribute("loggedUser");
//        if (entity == null) {
//            return "redirect:/signin";
//        } else {
//            Long userId = SecurityUtils.getCurrentUser().getUser().getId();
//            List<OrderPendingDTO> order = orderService.getPendingPaymentByUserId(userId);
//            System.out.println("order: " + order);
//            model.addAttribute("order", order);
//            return "payments/pending-payment";
//        }
//    }

}
