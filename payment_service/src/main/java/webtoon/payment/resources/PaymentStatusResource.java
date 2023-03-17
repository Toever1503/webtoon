package webtoon.payment.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import webtoon.payment.models.PaymentStatusModel;
import webtoon.payment.services.IPaymentStatusService;

@RestController
@RequestMapping("/payment_status")
public class PaymentStatusResource {
    @Autowired
    private IPaymentStatusService paymentStatusService;

    public PaymentStatusResource(IPaymentStatusService paymentStatusService) {
        super();
        this.paymentStatusService = paymentStatusService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addPaymentStatus(@RequestBody PaymentStatusModel paymentStatusModel){
        return ResponseEntity.ok(paymentStatusService.add(paymentStatusModel));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updatePaymentStatus(@PathVariable Long id, @RequestBody PaymentStatusModel paymentStatusModel){
        paymentStatusModel.setId(id);
        return ResponseEntity.ok(paymentStatusService.update(paymentStatusModel));
    }
}
