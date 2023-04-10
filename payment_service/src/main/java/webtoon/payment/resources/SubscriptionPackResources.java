package webtoon.payment.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import webtoon.payment.dtos.SubscriptionPackDto;
import webtoon.payment.entities.SubscriptionPackEntity;
import webtoon.payment.models.SubscriptionPackModel;
import webtoon.payment.services.ISubscriptionPackService;

import java.util.List;

@Controller
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
    public String getByPrice(@PathVariable Integer price, Model model) {
        SubscriptionPackEntity subscriptionPackEntity = this.subscriptionPackService.getByPrice(Double.parseDouble(price.toString()));
        System.out.println(subscriptionPackEntity);
        model.addAttribute("id", subscriptionPackEntity.getId());
        model.addAttribute("items", subscriptionPackEntity.getPrice());
        return "chonPTTT";
    }
    @GetMapping("/load")
    public String Payment(Model model) {
        List<SubscriptionPackDto> subscriptionPackEntities = this.subscriptionPackService.getAll();
        System.out.println(subscriptionPackEntities + "test");
        model.addAttribute("items", subscriptionPackEntities);
        return "chonGoi";
    }

}
