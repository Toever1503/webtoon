package webtoon.payment.resources;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;
import webtoon.payment.dtos.OrderDto;
import webtoon.payment.entities.OrderEntity;
import webtoon.payment.entities.OrderEntity_;
import webtoon.payment.inputs.OrderFilterInput;
import webtoon.payment.inputs.OrderInput;
import webtoon.payment.inputs.UpgradeOrderInput;
import webtoon.payment.services.IOrderService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/orders")
public class OrderResource {


    private final IOrderService orderService;

    public OrderResource(IOrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/filter")
    public Page<OrderDto> filter(@RequestBody OrderFilterInput input, Pageable pageable) {
        List<Specification<OrderEntity>> specs = new ArrayList<>();
        specs.add((root, query, criteriaBuilder) -> criteriaBuilder.isNull(root.get(OrderEntity_.DELETED_AT)));
        if (input.getQ() != null) {
            input.setQ("%" + input.getQ() + "%");
            specs.add((root, query, cb) -> cb.or(
                    cb.like(root.get(OrderEntity_.MA_DON_HANG), input.getQ())
            ));
        }
        if(input.getStatus() != null){
            specs.add((root, query, cb) -> cb.equal(root.get(OrderEntity_.STATUS), input.getStatus()));
        }
        Specification<OrderEntity> finalSpec = null;
        for(Specification<OrderEntity> spec : specs){
            if(finalSpec == null){
                finalSpec = Specification.where(spec);
            }else{
                finalSpec = finalSpec.and(spec);
            }
        }
        return this.orderService.filter(pageable, finalSpec);
    }

    @PutMapping("{id}")
    public OrderDto updateOrder(@PathVariable Long id, @RequestBody OrderInput input){
        input.setId(id);

        return this.orderService.updateOrder(input);
    }

    @PostMapping("upgrade-order")
    public OrderDto upgradeOrder(@RequestBody UpgradeOrderInput input){
        return this.orderService.upgradeOrder(input);
    }

    @PostMapping
    public OrderDto addOrder(@RequestBody OrderInput input){
        return this.orderService.addNewOrder(input);
    }

    @DeleteMapping("{id}")
    public void deleteById(@PathVariable Long id){
        this.orderService.deleteById(id);
    }


}
