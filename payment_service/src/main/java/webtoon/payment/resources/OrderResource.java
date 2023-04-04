package webtoon.payment.resources;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import webtoon.payment.dtos.OrderDto;
import webtoon.payment.dtos.SubscriptionPackDto;
import webtoon.payment.entities.OrderEntity;
import webtoon.payment.models.OrderModel;
import webtoon.payment.services.IOrderService;
import webtoon.payment.services.ISubscriptionPackService;

import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderResource {
    @Autowired
    private ISubscriptionPackService subscriptionPackService;
    @Autowired
    private IOrderService orderService;

    public OrderResource(IOrderService orderService) {
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
    @GetMapping("/load")
    public String Payment(Model model) {
        List<SubscriptionPackDto> subscriptionPackEntities = this.subscriptionPackService.getAll();
        System.out.println(subscriptionPackEntities + "test");
        model.addAttribute("items", subscriptionPackEntities);
        return "chonGoi";
    }
}
