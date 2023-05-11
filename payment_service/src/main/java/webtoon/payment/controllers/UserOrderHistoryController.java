package webtoon.payment.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import webtoon.account.configs.security.SecurityUtils;
import webtoon.account.entities.UserEntity;
import webtoon.account.entities.UserEntity_;
import webtoon.payment.entities.OrderEntity;
import webtoon.payment.entities.OrderEntity_;
import webtoon.payment.entities.SubscriptionPackEntity_;
import webtoon.payment.enums.EOrderStatus;
import webtoon.payment.services.IOrderService;
import webtoon.payment.services.ISubscriptionPackService;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserOrderHistoryController {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private ISubscriptionPackService subscriptionPackService;

    @GetMapping("/userOrder")
    public String userOrder(Model model, HttpSession session) {
        UserEntity entity = (UserEntity) session.getAttribute("loggedUser");
        if (entity == null) {
            return "redirect:/signin";
        } else {
            Long userId = SecurityUtils.getCurrentUser().getUser().getId();
            UserEntity userEntity = SecurityUtils.getCurrentUser().getUser();
            List<OrderEntity> order = orderService.getPaymentCompletedByUserId(userId);
            model.addAttribute("user", userEntity);
            model.addAttribute("order", order);
            return "account/userOrder";
        }
    }

    @GetMapping("/order-history")
    public String confirmOrder(Model model, HttpSession session,
                               @RequestParam(name = "search", required = false, defaultValue = "") String search,
                               @RequestParam("status") EOrderStatus status,
                               Pageable pageable) {
        UserEntity entity = (UserEntity) session.getAttribute("loggedUser");
        if (entity == null) {
            return "redirect:/signin";
        } else {
            List<Specification<OrderEntity>> specs = new ArrayList<>();
            specs.add((root, query, criteriaBuilder) -> criteriaBuilder.isNull(root.get(OrderEntity_.DELETED_AT)));
            specs.add((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(OrderEntity_.STATUS), EOrderStatus.DRAFTED).not());
            specs.add((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.join(OrderEntity_.USER_ID).get(UserEntity_.ID), entity.getId()));

            if (!search.isBlank()) {
                search = "%" + search + "%";
                String finalSearch = search;
                specs.add((root, query, cb) -> cb.or(
                        cb.like(root.get(OrderEntity_.MA_DON_HANG), finalSearch),
                        cb.like(root.get(OrderEntity_.SUBS_PACK_ID).get(SubscriptionPackEntity_.NAME), finalSearch)
                ));
            }
            if (status != null) {
                specs.add((root, query, cb) -> cb.equal(root.get(OrderEntity_.STATUS), status));
            }
            Specification<OrderEntity> finalSpec = null;
            for (Specification<OrderEntity> spec : specs) {
                if (finalSpec == null) {
                    finalSpec = Specification.where(spec);
                } else {
                    finalSpec = finalSpec.and(spec);
                }
            }
            Page<OrderEntity> orderPage = orderService.filterEntity(pageable, finalSpec);
            model.addAttribute("search", search);
            model.addAttribute("status", status.name());
            model.addAttribute("user", entity);
            model.addAttribute("order", orderPage.getContent());


            model.addAttribute("hasPrevPage", orderPage.hasPrevious());
            model.addAttribute("hasNextPage", orderPage.hasNext());
            model.addAttribute("currentPage", orderPage.getNumber());
            model.addAttribute("totalPage", orderPage.getTotalPages());
            return "account/userOrder";
        }
    }

    @GetMapping("subscription-status")
    public String subscriptionStatus(Model model, HttpSession session) {
        UserEntity entity = (UserEntity) session.getAttribute("loggedUser");
        if (entity == null) {
            return "redirect:/signin";
        } else {
            boolean isUsingTrial = false;
            UserEntity userEntity = (UserEntity) session.getAttribute("loggedUser");

            if (userEntity.getCurrentUsedSubsId() == null && userEntity.getTrialRegisteredDate() != null) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                int result = formatter.format(userEntity.getCanReadUntilDate()).compareTo(formatter.format(Calendar.getInstance().getTime()));
                isUsingTrial = result >= 0;
            }

            model.addAttribute("isUsingTrial", isUsingTrial);
            if (userEntity.getCurrentUsedSubsId() != null)
                model.addAttribute("currentUsingSubsPack", this.subscriptionPackService.getById(userEntity.getCurrentUsedSubsId()));

            return "payments/user/subscriptionStatus";
        }
    }


}
