package webtoon.domains.main.loader.payment.resources;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import webtoon.payment.resources.PaymentResource;
import webtoon.payment.services.IPaymentService;

@RestController
@RequestMapping("/payment")
public class PaymentResourceLoader extends PaymentResource {

    public PaymentResourceLoader(IPaymentService paymentService) {
        super(paymentService);
    }
}
