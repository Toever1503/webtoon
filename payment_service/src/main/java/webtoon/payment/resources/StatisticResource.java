package webtoon.payment.resources;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import webtoon.account.dtos.UserMetadataDto;
import webtoon.account.entities.UserEntity;
import webtoon.account.entities.UserEntity_;
import webtoon.account.services.IUserService;
import webtoon.payment.dtos.SubscriptionPackDto;
import webtoon.payment.dtos.UserSubscriptionPackStatusDto;
import webtoon.payment.entities.OrderEntity_;
import webtoon.payment.enums.EOrderStatus;
import webtoon.payment.services.IOrderService;
import webtoon.payment.services.ISubscriptionPackService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api/statistics")
public class StatisticResource {


    private final IOrderService orderService;

    private final IUserService userService;

    private final ISubscriptionPackService subscriptionPackService;

    public StatisticResource(IOrderService orderService, IUserService userService, ISubscriptionPackService subscriptionPackService) {
        this.orderService = orderService;
        this.userService = userService;
        this.subscriptionPackService = subscriptionPackService;
    }

    @GetMapping("sum-completed-order-this-month")
    public Long countTotalCompletedOrderThisMonth(){
        return this.orderService.countTotalCompletedOrderThisMonth();
    }

    @GetMapping("sum-revenue-this-month")
    public Long sumRevenueThisMonth() {
        return this.orderService.totalRevenueThisMonth();
    }

    @GetMapping("count-total-register-this-month")
    public Long countTotalRegisterThisMonth() {
        return this.orderService.countTotalRegisterThisMonth();
    }

    @GetMapping("count-total-register-trial-this-month")
    public Long countTotalRegisterTrialThisMonth() {
        return this.userService.countTotalRegisterTrialThisMonth();
    }

    @GetMapping("sum-revenue-per-day-by-month")
    public List<Object[]> sumRevenuePerDayByMonth(@RequestParam String monthDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
        return this.orderService.sumRevenuePerDayByMonth(monthDate);
    }
    @GetMapping("sum-revenue-per-month-by-year")
    public List<Object[]> sumRevenuePerMonthByYear(@RequestParam String year) {
        return this.orderService.sumRevenuePerMonthByYear(year);
    }

    @GetMapping("sum-revenue-per-subsPack-by-month")
    public List<Object[]> sumRevenuePerSubsPackByMonth(@RequestParam String monthDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
        return this.orderService.sumRevenuePerSubsPackByMonth(monthDate);
    }

    @GetMapping("count-total-subscriber-status-per-month-by-year")
    public List<Object[]> countSubscriberStatusPerMonthByYear(@RequestParam String year) {
        return this.orderService.countSubscriberStatusPerMonthByYear(year);
    }


    @GetMapping("filter-user-subscriptionPack-status")
    public Page<UserSubscriptionPackStatusDto> filterUserSubscriptionPackStatus(Pageable pageable,
                                                                                @RequestParam(required = false, defaultValue = "") String q,
                                                                                @RequestParam(required = false, defaultValue = "ALL") String type
    ) {
        List<Specification> specs = new ArrayList<>();
        specs.add(((root, query, criteriaBuilder) -> criteriaBuilder.isNull(root.get(UserEntity_.DELETED_AT))));
        specs.add(((root, query, criteriaBuilder) -> criteriaBuilder.isNull(root.get(UserEntity_.CAN_READ_UNTIL_DATE)).not()));
        specs.add(((root, query, criteriaBuilder) -> criteriaBuilder.isNull(root.get(UserEntity_.CURRENT_USED_SUBS_ID)).not()));

        switch (type) {
            case "EXPIRING": {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, 4);
                Date curentDate = Calendar.getInstance().getTime();
                specs.add(((root, query, criteriaBuilder) -> criteriaBuilder.between(root.get(UserEntity_.CAN_READ_UNTIL_DATE), curentDate, calendar.getTime())));
                break;
            }
            case "EXPIRED": {
                specs.add(((root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get(UserEntity_.CAN_READ_UNTIL_DATE), Calendar.getInstance().getTime())));
                break;
            }
            default: {
                break;
            }
        }

        if (!q.isBlank()) {
            final String qSearch = "%" + q + "%";
            specs.add((root, query, criteriaBuilder) -> criteriaBuilder.or(
                    criteriaBuilder.like(root.get(UserEntity_.FULL_NAME), qSearch),
                    criteriaBuilder.like(root.get(UserEntity_.USERNAME), qSearch),
                    criteriaBuilder.like(root.get(UserEntity_.EMAIL), qSearch),
                    criteriaBuilder.like(root.get(UserEntity_.PHONE), qSearch)
            ));
        }

        Specification finalSpec = null;
        for (Specification spec : specs) {
            if (finalSpec == null) {
                finalSpec = Specification.where(spec);
            } else {
                finalSpec = finalSpec.and(spec);
            }
        }

        Page<UserEntity> userEntityPage = this.userService.findAllEntities(pageable, finalSpec);

        List<UserSubscriptionPackStatusDto> userSubscriptionPackStatusList = new ArrayList<>();

        userEntityPage.getContent().stream().forEach(userEntity -> {
            UserSubscriptionPackStatusDto dto = UserSubscriptionPackStatusDto.builder()
                    .userId(userEntity.getId())
                    .user(UserMetadataDto.toDto(userEntity))
                    .expiredDate(userEntity.getCanReadUntilDate())
                    .hasSendRenewalEmail(userEntity.getHasSendRenewalEmail())
                    .build();

            dto.setSubscriptionPack(SubscriptionPackDto.toDto(this.subscriptionPackService.getById(userEntity.getCurrentUsedSubsId())));
            userSubscriptionPackStatusList.add(dto);
        });

        PageImpl<UserSubscriptionPackStatusDto> userSubscriptionPackStatusPage = new PageImpl<>(userSubscriptionPackStatusList, userEntityPage.getPageable(), userEntityPage.getTotalElements());
        return userSubscriptionPackStatusPage;
    }

}
