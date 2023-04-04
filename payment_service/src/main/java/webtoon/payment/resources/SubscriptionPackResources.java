package webtoon.payment.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import webtoon.payment.models.SubscriptionPackModel;
import webtoon.payment.services.ISubscriptionPackService;

@RestController
@RequestMapping("/subscription_pack")
public class SubscriptionPackResources {
    @Autowired
    private ISubscriptionPackService subscriptionPackService;

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(this.subscriptionPackService.getAll());
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody SubscriptionPackModel subscriptionPackModel) {
        return ResponseEntity.ok(this.subscriptionPackService.addSubscriptionPack(subscriptionPackModel));
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody SubscriptionPackModel subscriptionPackModel) {
        return ResponseEntity.ok(this.subscriptionPackService.updateSubscriptionPack(subscriptionPackModel));
    }

    @GetMapping("/getByPrice/{price}")
    public ResponseEntity<?> getByPrice(@PathVariable Integer price) {
        return ResponseEntity.ok(this.subscriptionPackService.getByPrice(Double.parseDouble(price.toString())));
    }

}
