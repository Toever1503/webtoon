package webtoon.payment.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import webtoon.account.configs.security.SecurityUtils;
import webtoon.account.entities.UserEntity;
import webtoon.payment.enums.EOrderStatus;
import webtoon.payment.enums.EOrderType;
import webtoon.payment.enums.EPaymentMethod;
import webtoon.payment.services.IOrderService;
import webtoon.payment.services.ISubscriptionPackService;
import webtoon.payment.entities.OrderEntity;
import webtoon.payment.entities.SubscriptionPackEntity;
import webtoon.payment.models.OrderModel;

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
    public String chuyenKhoan(@PathVariable Long id, Model model) {
//        SubscriptionPackEntity subscriptionPackEntity = this.subscriptionPackService.getByPrice(orderModel.getFinalPrice());
        String maDonHang = VnPayConfig.getRandomNumber(8);
        Date createDate = new Date();
        SubscriptionPackEntity subscriptionPackEntity = subscriptionPackService.getById(id);
        UserEntity userEntity = SecurityUtils.getCurrentUser().getUser();
//        System.out.println("price: "+subscriptionPackEntity.getPrice());
        model.addAttribute("name", subscriptionPackEntity.getName());
        Double price = subscriptionPackEntity.getPrice();
        model.addAttribute("price", price);
        model.addAttribute("maDonHang", maDonHang);

        orderService.add(new OrderModel(id, createDate, createDate, price, EOrderType.NEW, EOrderStatus.PENDING_PAYMENT, "CHUYENKHOAN", "0:0:0:0:0:0:0:1", maDonHang, subscriptionPackEntity,userEntity, EPaymentMethod.ATM));

        return "payments/chuyenKhoan";
    }

    @GetMapping("/userOrder/{id}")
    public String userOrder(@PathVariable Long id,Model model) {
        Long userId = SecurityUtils.getCurrentUser().getUser().getId();
        List<OrderEntity> order = orderService.getByUserId(userId);
        System.out.println("order: "+order);
        model.addAttribute("order", order);
        return "payments/userOrder";
    }

}
