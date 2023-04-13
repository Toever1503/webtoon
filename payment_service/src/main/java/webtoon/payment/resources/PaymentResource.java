package webtoon.payment.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import webtoon.payment.services.IPaymentService;
import webtoon.payment.entities.PaymentEntity;
import webtoon.payment.models.PaymentModel;

import java.util.List;

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
    public ResponseEntity<?> addPayment(@RequestBody PaymentEntity paymentModel) {
        return ResponseEntity.ok(paymentService.add(paymentModel));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updatePayment(@RequestBody PaymentModel paymentModel){
        return ResponseEntity.ok(paymentService.update(paymentModel));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<PaymentEntity>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAll());
    }
}
