package webtoon.main.payment.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import webtoon.main.payment.dtos.OrderDto;
import webtoon.main.payment.dtos.OrderPendingDTO;
import webtoon.main.payment.entities.OrderEntity;
import webtoon.main.payment.entities.SubscriptionPackEntity;
import webtoon.main.payment.enums.EOrderStatus;
import webtoon.main.payment.enums.EPaymentMethod;
import webtoon.main.payment.inputs.OrderInput;
import webtoon.main.payment.inputs.UpgradeOrderInput;
import webtoon.main.payment.models.OrderModel;

import javax.mail.MessagingException;
import java.util.List;

public interface IOrderService {
    OrderDto add(OrderModel orderModel);
    OrderDto update(OrderModel orderModel);
//    Page<OrderDto> getAll(Pageable pageable, Specification<OrderEntity> specification);
    List<OrderEntity> getAll();
    OrderEntity getById(Long id);
    OrderEntity getMaDonHang(String maDonHang);

    Page<OrderDto> filter(Pageable pageable, Specification<OrderEntity> finalSpec);
    Page<OrderEntity> filterEntity(Pageable pageable, Specification<OrderEntity> finalSpec);
    List<OrderEntity> getByUserId(Long userId);
    Long getIdByMaDonHang(String maDonHang);


    // for api
    OrderDto addNewOrder(OrderInput input);

    OrderDto updateOrder(OrderInput input);

    void deleteById(Long id);

    OrderDto upgradeOrder(UpgradeOrderInput input);

    List<OrderPendingDTO> getPendingPaymentByUserId(Long userId);
    List<OrderEntity> getPaymentCompletedByUserId(Long userId);
    List<OrderEntity> getPaymentConfirmByUserId(Long userId, EOrderStatus status);
    List<OrderEntity> searchKeyWord(Long userId, EOrderStatus status, String search);

    OrderEntity createDraftedOrder(SubscriptionPackEntity subscriptionPackEntity, EPaymentMethod paymentMethod);

    void userConfirmOrder(String maDonHang);

    OrderEntity changeStatus(Long orderId, EOrderStatus status);

    void saveOrderEntity(OrderEntity orderEntity);

    void cancelOrder(Long id);

    void returnOrder(Long id);

    Long countTotalOrderInToday();

    Long sumTotalRevenueInToday();

    Long countTotalPaymentPendingInToday();

    Long countTotalCompletedOrderInToday();

    Long countTotalCanceledOrderInToday();

    List<Object[]> sumTotalRevenueInLast7Days();

    void changeStatusOrder(Long id, EOrderStatus status);

    Long totalRevenueThisMonth();

    Long countTotalNewRegisterThisMonth();

    List<Object[]> sumRevenuePerMonthByYear(String year);

    List<Object[]> sumRevenuePerDayByMonth(String monthDate);

    List<Object[]> sumRevenuePerSubsPackByMonth(String monthDate);

    List<Object[]> countSubscriberStatusPerMonthByYear(String year);

    OrderDto findById(Long id);

    Long countTotalCompletedOrderThisMonth();

    void sendMailRenewSubscription(Long userId) throws MessagingException;

    void plusReadTimeToUser(OrderEntity orderEntity);

    Long count(Specification<OrderEntity> spec);

    List<Object[]> countTotalPeoplePerSubsPackByMonth(String monthDate);
}
