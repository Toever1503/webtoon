package webtoon.domains.main.loader.payment.resources;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import webtoon.payment.resources.PaymentStatusResource;
import webtoon.payment.services.IPaymentStatusService;

@RestController
@RequestMapping("/payment_status")
public class PaymentStatusResourceLoader extends PaymentStatusResource {
    public PaymentStatusResourceLoader(IPaymentStatusService paymentStatusService) {
        super(paymentStatusService);
    }
}
