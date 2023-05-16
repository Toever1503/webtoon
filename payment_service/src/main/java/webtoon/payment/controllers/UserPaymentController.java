package webtoon.payment.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.expression.Dates;
import webtoon.account.configs.security.SecurityUtils;
import webtoon.account.entities.UserEntity;
import webtoon.account.entities.UserEntity_;
import webtoon.account.services.IUserService;
import webtoon.payment.dtos.OrderDto;
import webtoon.payment.entities.OrderEntity;
import webtoon.payment.entities.OrderEntity_;
import webtoon.payment.entities.SubscriptionPackEntity;
import webtoon.payment.entities.SubscriptionPackEntity_;
import webtoon.payment.enums.EOrderStatus;
import webtoon.payment.enums.EOrderType;
import webtoon.payment.enums.EPaymentMethod;
import webtoon.payment.services.IOrderService;
import webtoon.payment.services.ISubscriptionPackService;
import webtoon.storage.domain.services.IFileService;

import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class UserPaymentController {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private ISubscriptionPackService subscriptionPackService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IFileService fileService;

    public UserPaymentController() {
        System.out.println("okk");
    }

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
                               @RequestParam(value = "status", defaultValue = "COMPLETED") EOrderStatus status,
                               Pageable pageable) {
        UserEntity loggedUser = (UserEntity) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/signin?redirectTo=/user/order-history";
        } else {
            List<Specification<OrderEntity>> specs = new ArrayList<>();
            specs.add((root, query, criteriaBuilder) -> criteriaBuilder.isNull(root.get(OrderEntity_.DELETED_AT)));
            specs.add((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(OrderEntity_.STATUS), EOrderStatus.DRAFTED).not());
            specs.add((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.join(OrderEntity_.USER_ID).get(UserEntity_.ID), loggedUser.getId()));

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

            Long pendingPaymentCount = this.orderService.count(((root, query, criteriaBuilder) ->
                    criteriaBuilder.and(
                            criteriaBuilder.equal(root.get(OrderEntity_.STATUS), EOrderStatus.PENDING_PAYMENT),
                            criteriaBuilder.equal(root.join(OrderEntity_.USER_ID).get(UserEntity_.ID), loggedUser.getId())
                    )));
            Long pendingCheckCount = this.orderService.count(((root, query, criteriaBuilder) ->
                    criteriaBuilder.and(
                            criteriaBuilder.equal(root.get(OrderEntity_.STATUS), EOrderStatus.USER_CONFIRMED_BANKING),
                            criteriaBuilder.equal(root.join(OrderEntity_.USER_ID).get(UserEntity_.ID), loggedUser.getId())
                    )
            ));

            model.addAttribute("search", search);
            model.addAttribute("status", status.name());
            model.addAttribute("user", loggedUser);
            model.addAttribute("order", orderPage.getContent());

            model.addAttribute("pendingPaymentCount", pendingPaymentCount);
            model.addAttribute("pendingCheckCount", pendingCheckCount);

            model.addAttribute("hasPrevPage", orderPage.hasPrevious());
            model.addAttribute("hasNextPage", orderPage.hasNext());
            model.addAttribute("currentPage", orderPage.getNumber());
            model.addAttribute("totalPage", orderPage.getTotalPages());
            return "account/userOrder";
        }
    }

    @GetMapping("subscription-status")
    public String subscriptionStatus(Model model, HttpSession session) {
        UserEntity loggedUser = (UserEntity) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/signin?redirectTo=/user/subscription-status";
        } else {
            if (loggedUser.getPhone() == null)
                return "redirect:/user/update_more_info";

            boolean isUsingTrial = false;
            boolean isTrialExpired = false;

            UserEntity userEntity = this.userService.getById(loggedUser.getId());
            session.setAttribute("loggedUser", userEntity);

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            if (userEntity.getCurrentUsedSubsId() == null && userEntity.getTrialRegisteredDate() != null) {
                int result = formatter.format(userEntity.getCanReadUntilDate()).compareTo(formatter.format(Calendar.getInstance().getTime()));
                isUsingTrial = result >= 0;

                if (userEntity.getCurrentUsedSubsId() == null)
                    isUsingTrial = true;
                isTrialExpired = result < 0;
            }

            boolean isExpiredSub = false;
            if (userEntity.getCurrentUsedSubsId() != null) {
                int result = formatter.format(userEntity.getCanReadUntilDate()).compareTo(formatter.format(Calendar.getInstance().getTime()));
                isExpiredSub = result < 0;
            }

            boolean canUpgradeSubs = false;
            if (userEntity.getCurrentUsedSubsId() != null) {
                isUsingTrial = false;
                model.addAttribute("currentUsingSubsPack", this.subscriptionPackService.getById(userEntity.getCurrentUsedSubsId()));

                List<SubscriptionPackEntity> upgradeList = this.subscriptionPackService.findAllEntity(null);

                Specification pendingOrderSpec = ((root, query, criteriaBuilder) -> criteriaBuilder.and(
                        criteriaBuilder.equal(root.get(OrderEntity_.STATUS), EOrderStatus.DRAFTED).not(),
                        root.get(OrderEntity_.STATUS).in(List.of(EOrderStatus.PENDING_PAYMENT, EOrderStatus.USER_CONFIRMED_BANKING)),
                        criteriaBuilder.equal(root.join(OrderEntity_.USER_ID).get(UserEntity_.ID), userEntity.getId())
                ));

                boolean hasOtherPendingOrder = false;
                Page<OrderDto> pendingOrderPage = this.orderService.filter(Pageable.unpaged(), pendingOrderSpec);
                if (!pendingOrderPage.getContent().isEmpty())
                    hasOtherPendingOrder = true;

                for (SubscriptionPackEntity sub : upgradeList) {
                    if (sub.getMonthCount() > userEntity.getCurrentUsedSubsId()) {
                        canUpgradeSubs = true;
                    }
                }

                model.addAttribute("canUpgradeSubs", canUpgradeSubs && !hasOtherPendingOrder);
            } else model.addAttribute("canUpgradeSubs", true);

            model.addAttribute("isTrialExpired", isTrialExpired);
            model.addAttribute("isExpiredSub", isExpiredSub);
            model.addAttribute("isUsingTrial", isUsingTrial);

            return "payments/user/subscriptionStatus";
        }
    }


    @GetMapping("upgrade-subscription")
    public String getFromUpgrade(Model model, HttpSession session) {
        UserEntity loggedUser = (UserEntity) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/signin?redirectTo=/user/subscription-status";
        }
        if (loggedUser.getPhone() == null)
            return "redirect:/user/update_more_info";
        loggedUser = this.userService.getById(SecurityUtils.getCurrentUser().getUser().getId());
        session.setAttribute("loggedUser", loggedUser);
        boolean isUsingTrial = false;
        if (loggedUser.getCurrentUsedSubsId() == null && loggedUser.getTrialRegisteredDate() != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            int result = formatter.format(loggedUser.getCanReadUntilDate()).compareTo(formatter.format(Calendar.getInstance().getTime()));
            isUsingTrial = result >= 0;
        }

        Double currentPrice = Double.valueOf(0);
        if (!isUsingTrial) {
            SubscriptionPackEntity subscriptionPack = this.subscriptionPackService.getById(loggedUser.getCurrentUsedSubsId());
            model.addAttribute("currentUsingSubsPack", subscriptionPack);
            currentPrice = subscriptionPack.getPrice();
        }
        model.addAttribute("currentPrice", currentPrice);
        model.addAttribute("isUsingTrial", isUsingTrial);

        List<SubscriptionPackEntity> upgradeList = this.subscriptionPackService.findAllEntity(null);
        List<SubscriptionPackEntity> canUpgradeList = new ArrayList<>();

        if (loggedUser.getCurrentUsedSubsId() != null) {
            UserEntity finalUserEntity = loggedUser;
            canUpgradeList = upgradeList.stream().filter(sub -> sub.getId() > finalUserEntity.getCurrentUsedSubsId()).collect(Collectors.toList());
        } else canUpgradeList = upgradeList;


        model.addAttribute("finalPrice", canUpgradeList.get(0).getPrice() - currentPrice);
        model.addAttribute("upgradeSubscriptionList", canUpgradeList);

        return "payments/user/upgradeSubscription";
    }

    @GetMapping("handle-upgrade-subscription")
    public String handleUpgradeSubscription(@RequestParam Long subscriptionId,
                                            @RequestParam EPaymentMethod paymentMethod,
                                            HttpSession session) {
        UserEntity loggedUser = (UserEntity) session.getAttribute("loggedUser");
        if (loggedUser.getPhone() == null)
            return "redirect:/user/update_more_info";
        SubscriptionPackEntity newUpgradeSub = this.subscriptionPackService.getById(subscriptionId);

        boolean isUsingTrial = false;

        if (loggedUser.getCurrentUsedSubsId() == null && loggedUser.getTrialRegisteredDate() != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            int result = formatter.format(loggedUser.getCanReadUntilDate()).compareTo(formatter.format(Calendar.getInstance().getTime()));
            isUsingTrial = result >= 0;
        }

        Double currentPrice = Double.valueOf(0);
        if (!isUsingTrial) { // for upgrade from existed order
            SubscriptionPackEntity subscriptionPack = this.subscriptionPackService.getById(loggedUser.getCurrentUsedSubsId());
            currentPrice = subscriptionPack.getPrice();
        }

        String orderNumber = VnPayConfig.getRandomNumber(10);
        while (this.orderService.getMaDonHang(orderNumber) != null) {
            orderNumber = VnPayConfig.getRandomNumber(10);
        }
        OrderEntity upgradeOrder = OrderEntity.builder()
                .orderType(EOrderType.UPGRADE)
                .maDonHang(orderNumber)
                .subs_pack_id(newUpgradeSub)
                .paymentMethod(paymentMethod)
                .status(EOrderStatus.DRAFTED)
                .finalPrice(newUpgradeSub.getPrice() - currentPrice)
                .user_id(SecurityUtils.getCurrentUser().getUser())
                .modifiedBy(SecurityUtils.getCurrentUser().getUser())
                .build();
        this.orderService.saveOrderEntity(upgradeOrder);
        if (paymentMethod.equals(EPaymentMethod.VN_PAY)) {
            return "redirect:/payment/pay?orderId=" + upgradeOrder.getId();
        }
        return "redirect:/order/bank-info/" + upgradeOrder.getId();
    }


    @GetMapping("renew_subscription_pack")
    public String renewSubscriptionPack(Model model, HttpSession session) {
        UserEntity loggedUser = (UserEntity) session.getAttribute("loggedUser");
        if (loggedUser == null)
            return "redirect:/signin?redirectTo=/user/renew_subscription_pack";
        if (loggedUser.getPhone() == null)
            return "redirect:/user/update_more_info";
        SubscriptionPackEntity subscriptionPack = this.subscriptionPackService.getById(loggedUser.getCurrentUsedSubsId());

        model.addAttribute("userEntity", loggedUser);
        model.addAttribute("sub", subscriptionPack);
        return "payments/user/renew_subscription_form";
    }

    @GetMapping("handle_renew_subscription_pack")
    public String handleRenew(@RequestParam EPaymentMethod paymentMethod, HttpSession session) {
        UserEntity loggedUser = (UserEntity) session.getAttribute("loggedUser");
        if (loggedUser == null)
            return "redirect:/signin?redirectTo=/user/renew_subscription_pack";
        if (loggedUser.getPhone() == null)
            return "redirect:/user/update_more_info";
        SubscriptionPackEntity subscriptionPackEntity = this.subscriptionPackService.getById(loggedUser.getCurrentUsedSubsId());
        OrderEntity orderEntity = this.orderService.createDraftedOrder(subscriptionPackEntity, paymentMethod);
        orderEntity.setOrderType(EOrderType.RENEW);
        this.orderService.saveOrderEntity(orderEntity);
        if (paymentMethod.equals(EPaymentMethod.VN_PAY)) {
            return "redirect:/payment/pay?orderId=" + orderEntity.getId();
        }
        return "redirect:/order/bank-info/" + orderEntity.getId();

    }

    @PostMapping("update-avatar")
    public String updateAvatar(@RequestPart MultipartFile avatar, HttpSession session, Model model) {
        UserEntity loggedUser = (UserEntity) session.getAttribute("loggedUser");
        if (loggedUser == null)
            return "redirect:/signin?redirectTo=/user/profile";
        try {
            loggedUser.setAvatar(fileService.uploadFile(avatar, "user" + loggedUser.getId() + "/").getUrl());
            this.userService.saveUserEntity(loggedUser);
            session.setAttribute("loggedUser", loggedUser);
            model.addAttribute("notifySuccessMessage", "Cập nhật ảnh đại diện thành công!");
            return "redirect:/user/profile";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error_message", "Cập nhật ảnh đại diện thất bại!");
        }
        return "redirect:/user/profile";
    }

    public static void main(String[] args) {
        Date d = java.sql.Date.valueOf("2023-05-20");
        Date d1 = java.sql.Date.valueOf("2023-05-19");

        System.out.println((d.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
        Dates dd;
    }
}
