package webtoon.payment.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import webtoon.account.configs.security.SecurityUtils;
import webtoon.account.entities.UserEntity;
import webtoon.account.entities.UserEntity_;
import webtoon.payment.entities.OrderEntity;
import webtoon.payment.entities.OrderEntity_;
import webtoon.payment.enums.EOrderStatus;
import webtoon.payment.services.IOrderService;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("/user")
public class OrderHistoryController {

    @Autowired
    private IOrderService orderService;

    @GetMapping("/userOrder")
    public String userOrder(Model model, Pageable pageable, HttpSession session) {
        UserEntity entity = (UserEntity) session.getAttribute("loggedUser");
        if (entity == null) {
            return "redirect:/signin";
        } else {
            Specification spec = Specification
                    .where(((root, query, criteriaBuilder) -> criteriaBuilder.isNull(root.get(OrderEntity_.DELETED_AT))))
                    .and(((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(OrderEntity_.STATUS), EOrderStatus.DRAFTED).not()))
                    .and(((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.join(OrderEntity_.USER_ID).get(UserEntity_.ID), entity.getId())))
                    ;

            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()).withSort(Sort.Direction.DESC, OrderEntity_.ID);
            Page<OrderEntity> orderEntityPage = this.orderService.filter(pageable, spec);

            UserEntity userEntity = SecurityUtils.getCurrentUser().getUser();

            Calendar canCancelOrReturnDate = Calendar.getInstance();
            canCancelOrReturnDate.add(Calendar.DATE, -3);


            Map<EOrderStatus, String> statusMap = new HashMap<>();
            statusMap.put(EOrderStatus.USER_CONFIRMED_BANKING, "Chờ xác nhận!");
            statusMap.put(EOrderStatus.REFUNDED, "Đã hoàn tiền!");
            statusMap.put(EOrderStatus.REFUNDING, "Chờ hoàn tiền!");
            statusMap.put(EOrderStatus.CANCELED, "Đã hủy!");
            statusMap.put(EOrderStatus.COMPLETED, "Hoàn tất!");

            model.addAttribute("user", userEntity);
            model.addAttribute("orders", orderEntityPage.getContent());
            model.addAttribute("canCancelOrReturnDate", canCancelOrReturnDate.getTime());
            model.addAttribute("statusMap", statusMap);
            model.addAttribute("currentPage", orderEntityPage.getNumber());

            return "account/userOrder";
        }
    }
}
