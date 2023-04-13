package webtoon.domains.main.loader.payment.resources;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import webtoon.payment.controller.PaymentController;

@Controller
@RequestMapping("payment/pay")
public class PaymentControllerLoader extends PaymentController {
}
