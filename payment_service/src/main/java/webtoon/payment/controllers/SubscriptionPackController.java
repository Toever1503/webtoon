package webtoon.payment.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import webtoon.account.entities.UserEntity;
import webtoon.payment.dtos.SubscriptionPackDto;
import webtoon.payment.entities.SubscriptionPackEntity_;
import webtoon.payment.services.IOrderService;
import webtoon.payment.services.ISubscriptionPackService;
import webtoon.payment.entities.SubscriptionPackEntity;
import webtoon.payment.models.SubscriptionPackModel;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@Controller
@RequestMapping("/subscription_pack")
public class SubscriptionPackController {
    @Autowired
    private ISubscriptionPackService subscriptionPackService;

    @Autowired
    private IOrderService orderService;

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
    public String getByPrice(@PathVariable Integer price, Model model, HttpSession session) {
        UserEntity entity = (UserEntity) session.getAttribute("loggedUser");
        if (entity == null) {
            return "redirect:/signin";
        } else {
            SubscriptionPackEntity subscriptionPackEntity = this.subscriptionPackService.getByPrice(Double.parseDouble(price.toString()));
            System.out.println(subscriptionPackEntity);
            model.addAttribute("id", subscriptionPackEntity.getId());
            model.addAttribute("items", subscriptionPackEntity.getPrice());
            return "payments/chonPTTT";
        }
    }

    @GetMapping("/load")
    public String Payment(Model model,
                          HttpSession session,
                          @RequestParam(required = false) String showNotification,
                          @RequestParam(required = false) String notificationMessage
    ) {
        Specification spec = (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.isNull(root.get(SubscriptionPackEntity_.DELETED_AT));
        List<SubscriptionPackDto> subscriptionPackEntities = this.subscriptionPackService.filter(
                PageRequest.of(0, 50).withSort(Sort.Direction.ASC, SubscriptionPackEntity_.MONTH_COUNT),
                spec
        ).getContent();

        boolean isUsingTrial = false;
        boolean hasExpiredTrial = false;
        UserEntity loggedUser = (UserEntity) session.getAttribute("loggedUser");

        boolean isExpiredSub = false;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        if(loggedUser != null){
            if (loggedUser.getPhone() == null)
                return "redirect:/user/update_more_info";
            if (loggedUser.getCurrentUsedSubsId() == null && loggedUser.getTrialRegisteredDate() != null) {
                int result = formatter.format(loggedUser.getCanReadUntilDate()).compareTo(formatter.format(Calendar.getInstance().getTime()));
                isUsingTrial = result >= 0;
                hasExpiredTrial = result < 0;
            }
            if (loggedUser.getCurrentUsedSubsId() != null) {
                int result = formatter.format(loggedUser.getCanReadUntilDate()).compareTo(formatter.format(Calendar.getInstance().getTime()));
                isExpiredSub = result < 0;
            }
        }

        model.addAttribute("isExpiredSub", isExpiredSub);
        model.addAttribute("items", subscriptionPackEntities);
        model.addAttribute("showNotification", showNotification);
        model.addAttribute("notificationMessage", notificationMessage);
        model.addAttribute("isUsingTrial", isUsingTrial);
        model.addAttribute("hasExpiredTrial", hasExpiredTrial);
        return "payments/chonGoi";
    }

}
