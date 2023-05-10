package webtoon.payment.resources;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import webtoon.account.services.IUserService;
import webtoon.payment.services.IOrderService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api/statistics")
public class StatisticResource {


    private final IOrderService orderService;

    private final IUserService userService;

    public StatisticResource(IOrderService orderService, IUserService userService) {
        this.orderService = orderService;
        this.userService = userService;
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

//    @GetMapping("count-total-register-per-day-by-month")
//    public List<Object[]> countTotalRegisterPerDayByMonth(@RequestParam Date monthDate) {
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
//        return this.orderService.countTotalRegisterPerDayByMonth(formatter.format(monthDate));
//    }

}
