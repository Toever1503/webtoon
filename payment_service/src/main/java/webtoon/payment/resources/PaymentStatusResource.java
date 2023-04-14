package webtoon.payment.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import webtoon.payment.services.IPaymentStatusService;
import webtoon.payment.models.PaymentStatusModel;

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
    public ResponseEntity<?> updatePaymentStatus(@RequestBody PaymentStatusModel paymentStatusModel){
        return ResponseEntity.ok(paymentStatusService.update(paymentStatusModel));
    }
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllPaymentStatus(){
        return ResponseEntity.ok(paymentStatusService.getAll());
    }
}
