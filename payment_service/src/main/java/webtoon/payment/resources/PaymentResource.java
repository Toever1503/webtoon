package webtoon.payment.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import webtoon.payment.models.PaymentModel;
import webtoon.payment.services.IPaymentService;

@RestController
@RequestMapping("/payment")
public class PaymentResource {
    @Autowired
    private IPaymentService paymentService;

    public PaymentResource(IPaymentService paymentService) {
        super();
        this.paymentService = paymentService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addPayment(@RequestBody PaymentModel paymentModel) {
        return ResponseEntity.ok(paymentService.add(paymentModel));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updatePayment(@PathVariable Long id, @RequestBody PaymentModel paymentModel){
        paymentModel.setId(id);
        return ResponseEntity.ok(paymentService.update(paymentModel));
    }
}
