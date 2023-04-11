package webtoon.domains.main.loader.payment.resources;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import webtoon.payment.resources.OrderResource;
import webtoon.payment.services.IOrderService;

@Controller
@RequestMapping("/order")
public class OrderResourceLoader extends OrderResource
{
    public OrderResourceLoader(IOrderService orderService) {
        super(orderService);
    }
}
