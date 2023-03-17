package webtoon.payment.resources;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import webtoon.payment.models.OrderModel;
import webtoon.payment.services.IOrderService;

@RestController
@RequestMapping("/order")
public class OrderResource {
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
    public ResponseEntity<?> updateOrder(@PathVariable Long id, @RequestBody OrderModel orderModel){
        orderModel.setId(id);
        return ResponseEntity.ok(orderService.update(orderModel));
    }
}
