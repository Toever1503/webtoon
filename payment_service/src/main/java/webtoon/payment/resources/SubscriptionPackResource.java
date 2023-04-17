package webtoon.payment.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import webtoon.payment.dtos.SubscriptionPackDto;
import webtoon.payment.services.ISubscriptionPackService;
import webtoon.payment.entities.SubscriptionPackEntity;
import webtoon.payment.models.SubscriptionPackModel;

import java.util.List;

@RestController
@RequestMapping("api/subscription-packs")
public class SubscriptionPackResource {
    @Autowired
    private ISubscriptionPackService subscriptionPackService;

    @PostMapping
    public SubscriptionPackDto add(@RequestBody SubscriptionPackModel subscriptionPackModel) {
        return this.subscriptionPackService.addSubscriptionPack(subscriptionPackModel);
    }

    @PutMapping("{id}")
    public SubscriptionPackDto update(@PathVariable Long id, @RequestBody SubscriptionPackModel subscriptionPackModel) {
        subscriptionPackModel.setId(id);
        return this.subscriptionPackService.updateSubscriptionPack(subscriptionPackModel);
    }


    @DeleteMapping("{id}")
    public void deleteById(@PathVariable Long id){
        this.subscriptionPackService.deleteById(id);
    }
}
