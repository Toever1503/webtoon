package webtoon.payment.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;
import webtoon.payment.dtos.SubscriptionPackDto;
import webtoon.payment.dtos.SubscriptionPackMetadataDto;
import webtoon.payment.inputs.SubscriptionPackFilterInput;
import webtoon.payment.services.ISubscriptionPackService;
import webtoon.payment.entities.SubscriptionPackEntity;
import webtoon.payment.models.SubscriptionPackModel;

import java.util.List;

@RestController
@RequestMapping("api/subscription-packs")
public class SubscriptionPackResource {
    @Autowired
    private ISubscriptionPackService subscriptionPackService;


    @GetMapping("get-all")
    public List<SubscriptionPackMetadataDto> getAll(){
        return this.subscriptionPackService.getAllPackMetadata();
    }

    @PostMapping("filter")
    public Page<SubscriptionPackDto> filter(@RequestBody SubscriptionPackFilterInput input, Pageable pageable){
        Specification<SubscriptionPackEntity> spec = null;
        if(input.getQ() != null)
            spec = Specification.where(((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + input.getQ() + "%")));
        return this.subscriptionPackService.filter(pageable, spec);
    }
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
