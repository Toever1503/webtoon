package webtoon.payment.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import webtoon.account.configs.security.SecurityUtils;
import webtoon.account.services.IUserService;
import webtoon.payment.controllers.VnPayConfig;
import webtoon.payment.dtos.OrderDto;
import webtoon.payment.dtos.OrderPendingDTO;
import webtoon.payment.entities.OrderEntity;
import webtoon.payment.entities.SubscriptionPackEntity;
import webtoon.payment.enums.EOrderStatus;
import webtoon.payment.enums.EOrderType;
import webtoon.payment.enums.EPaymentMethod;
import webtoon.payment.inputs.OrderInput;
import webtoon.payment.inputs.UpgradeOrderInput;
import webtoon.payment.models.OrderModel;
import webtoon.payment.repositories.IOrderRepository;
import webtoon.payment.services.IOrderService;
import webtoon.payment.services.ISendEmail;
import webtoon.payment.services.ISubscriptionPackService;
import webtoon.utils.exception.CustomHandleException;

import java.util.*;

@Service
@Transactional
public class OrderServiceImpl implements IOrderService {


    private final IOrderRepository orderRepository;

    @Autowired
    private ISubscriptionPackService subscriptionPackService;

    @Autowired
    private IUserService userService;

    private final ISendEmail sendEmailService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public OrderServiceImpl(IOrderRepository orderRepository, ISendEmail sendEmailService) {
        this.orderRepository = orderRepository;
        this.sendEmailService = sendEmailService;
    }

    @Override
    public OrderDto add(OrderModel orderModel) {
        OrderEntity orderEntity = OrderEntity.builder()
                .created_at(orderModel.getCreated_at())
                .finalPrice(orderModel.getFinalPrice())
                .orderType(orderModel.getEstatus())
                .status(orderModel.getStatus())
                .maDonHang(orderModel.getMaDonHang())
                .subs_pack_id(orderModel.getSubs_pack_id())
                .user_id(orderModel.getUser_id())
                .paymentMethod(orderModel.getPayment_method())
                .build();
        this.orderRepository.saveAndFlush(orderEntity);
        return OrderDto.toDto(orderEntity);
    }

    //
    @Override
    public OrderDto update(OrderModel orderModel) {
        OrderEntity orderEntity = this.getById(orderModel.getId());
        orderEntity.setCreated_at(orderModel.getCreated_at());
        orderEntity.setFinalPrice(orderModel.getFinalPrice());
        orderEntity.setOrderType(orderModel.getEstatus());
        orderEntity.setStatus(orderModel.getStatus());
        orderEntity.setMaDonHang(orderModel.getMaDonHang());
        orderEntity.setSubs_pack_id(orderModel.getSubs_pack_id());
        orderEntity.setUser_id(orderModel.getUser_id());
        orderEntity.setPaymentMethod(orderModel.getPayment_method());
        this.orderRepository.saveAndFlush(orderEntity);
        return OrderDto.toDto(orderEntity);
    }

    //    @Override
//    public Page<OrderDto> getAll(Pageable pageable, Specification<OrderEntity> specification) {
//        return orderRepository.findAll(specification, pageable).map(OrderDto::toDto);
//    }
//
    @Override
    public List<OrderEntity> getAll() {
        return this.orderRepository.findAll();
    }

    @Override
    public OrderEntity getById(Long id) {
        return this.orderRepository.findById(id).orElseThrow(() -> new CustomHandleException(41));
    }

    @Override
    public OrderEntity getMaDonHang(String maDonHang) {
        return this.orderRepository.getByMaDonHang(maDonHang);
    }

    @Override
    public Page<OrderDto> filter(Pageable pageable, Specification<OrderEntity> finalSpec) {
        return this.orderRepository.findAll(finalSpec, pageable).map(OrderDto::toDto);
    }

    @Override
    public Page<OrderEntity> filterEntity(Pageable pageable, Specification<OrderEntity> finalSpec) {
        return this.orderRepository.findAll(finalSpec, pageable);
    }

    public List<OrderEntity> getByUserId(Long userId) {
        return orderRepository.getByUserId(userId);
    }

    @Override
    public Long getIdByMaDonHang(String maDonHang) {
        return orderRepository.getIdByMaDonHang(maDonHang);
    }

    @Override
    public OrderDto addNewOrder(OrderInput input) {
        OrderEntity entity = OrderInput.toEntity(input);

        SubscriptionPackEntity subscriptionPack = this.subscriptionPackService.getById(input.getSubscriptionPack());

        String orderNumber = VnPayConfig.getRandomNumber(10);
        while (this.orderRepository.getByMaDonHang(orderNumber) != null) {
            orderNumber = VnPayConfig.getRandomNumber(10);
        }
        entity.setSubs_pack_id(subscriptionPack);
        entity.setFinalPrice(subscriptionPack.getPrice());

        entity.setMaDonHang(orderNumber);
        entity.setUser_id(userService.getById(input.getUser_id()));

        entity.setModifiedBy(SecurityUtils.getCurrentUser().getUser());
        this.orderRepository.saveAndFlush(entity);

        if (input.getStatus().equals(EOrderStatus.PENDING_PAYMENT)) {

        }

        return OrderDto.toDto(entity);
    }

    @Override
    public OrderDto updateOrder(OrderInput input) {
        OrderEntity entity = this.getById(input.getId());


        if (!entity.getSubs_pack_id().getId().equals(input.getSubscriptionPack())) {
            SubscriptionPackEntity subscriptionPack = this.subscriptionPackService.getById(input.getSubscriptionPack());

            entity.setSubs_pack_id(subscriptionPack);
            entity.setFinalPrice(subscriptionPack.getPrice());

        }

        entity.setOrderType(input.getOrderType());
        entity.setStatus(input.getStatus());
        entity.setPaymentMethod(input.getPaymentMethod());

        entity.setModifiedBy(SecurityUtils.getCurrentUser().getUser());

        // send order info to user's mail
        if (entity.getStatus().equals(EOrderStatus.COMPLETED))
            this.sendOrderInfoToMail(entity);

        this.orderRepository.saveAndFlush(entity);
        return OrderDto.toDto(entity);
    }

    @Override
    public void deleteById(Long id) {
        OrderEntity entity = this.getById(id);
        entity.setDeletedAt(Calendar.getInstance().getTime());
        this.orderRepository.saveAndFlush(entity);
    }

    @Override
    public List<OrderPendingDTO> getPendingPaymentByUserId(Long userId) {
        return this.orderRepository.getPendingPaymentByUserId(userId);
    }

    @Override
    public List<OrderEntity> getPaymentCompletedByUserId(Long userId) {
        return this.orderRepository.getPaymentCompletedByUserId(userId);
    }

    @Override
    public List<OrderEntity> getPaymentConfirmByUserId(Long userId, EOrderStatus status) {
        return this.orderRepository.getPaymentConfirmByUserId(userId, status);
    }

    @Override
    public List<OrderEntity> searchKeyWord(Long userId, EOrderStatus status, String search) {
        return this.orderRepository.searchByUserId(userId, status, search);
    }

    @Override
    public OrderEntity createDraftedOrder(SubscriptionPackEntity subscriptionPackEntity, EPaymentMethod paymentMethod) {
        String maDonHang = VnPayConfig.getRandomNumber(10);

        while (this.orderRepository.getByMaDonHang(maDonHang) != null) {
            maDonHang = VnPayConfig.getRandomNumber(10);
        }

        OrderEntity entity = OrderEntity.builder().build();
        entity.setSubs_pack_id(subscriptionPackEntity);
        entity.setFinalPrice(subscriptionPackEntity.getPrice());

        entity.setMaDonHang(maDonHang);
        entity.setUser_id(SecurityUtils.getCurrentUser().getUser());
        entity.setStatus(EOrderStatus.DRAFTED);
        entity.setOrderType(EOrderType.NEW);
        entity.setPaymentMethod(paymentMethod);
        entity.setModifiedBy(SecurityUtils.getCurrentUser().getUser());
        this.orderRepository.saveAndFlush(entity);
        return entity;
    }

    @Override
    public void userConfirmOrder(String maDonHang) {
        OrderEntity orderEntity = this.orderRepository.getByMaDonHang(maDonHang);
        if (orderEntity == null)
            throw new CustomHandleException(121);

        orderEntity.setStatus(EOrderStatus.USER_CONFIRMED_BANKING);
        orderEntity.setModifiedBy(SecurityUtils.getCurrentUser().getUser());

        this.orderRepository.saveAndFlush(orderEntity);
    }

    @Override
    public OrderEntity changeStatus(Long orderId, EOrderStatus status) {
        OrderEntity orderEntity = this.getById(orderId);
        orderEntity.setStatus(status);
        this.orderRepository.saveAndFlush(orderEntity);
        return orderEntity;
    }

    @Override
    public void saveOrderEntity(OrderEntity orderEntity) {
        this.orderRepository.saveAndFlush(orderEntity);
    }

    @Override
    public void cancelOrder(Long id) {
        OrderEntity orderEntity = this.getById(id);
        if (!orderEntity.getUser_id().getId().equals(SecurityUtils.getCurrentUser().getUser().getId()))
            throw new CustomHandleException(42);
        orderEntity.setStatus(EOrderStatus.CANCELED);
        this.orderRepository.saveAndFlush(orderEntity);
    }

    @Override
    public void returnOrder(Long id) {
        OrderEntity orderEntity = this.getById(id);
        if (!orderEntity.getUser_id().getId().equals(SecurityUtils.getCurrentUser().getUser().getId()))
            throw new CustomHandleException(43);
        this.orderRepository.saveAndFlush(orderEntity);
    }

    @Override
    public Long countTotalOrderInToday() {
        return this.orderRepository.countTotalOrderInToday();
    }

    @Override
    public Long sumTotalRevenueInToday() {
        return this.orderRepository.sumTotalRevenueInToday();
    }

    @Override
    public Long countTotalPaymentPendingInToday() {
        return this.orderRepository.countTotalPaymentPendingInToday();
    }

    @Override
    public Long countTotalCompletedOrderInToday() {
        return this.orderRepository.countTotalCompletedOrderInToday();
    }

    @Override
    public Long countTotalCanceledOrderInToday() {
        return this.orderRepository.countTotalCanceledOrderInToday();
    }

    @Override
    public List<Object[]> sumTotalRevenueInLast7Days() {
        return this.orderRepository.sumTotalRevenueInLast7Days();
    }

    @Override
    public void changeStatusOrder(Long id, EOrderStatus status) {
        OrderEntity orderEntity = this.getById(id);
        orderEntity.setStatus(status);
        if (status.equals(EOrderStatus.COMPLETED)) { // need send mail
            this.sendOrderInfoToMail(orderEntity);
        }
        this.orderRepository.saveAndFlush(orderEntity);
    }

    @Override
    public Long totalRevenueThisMonth() {
        return this.orderRepository.totalRevenueThisMonth();
    }

    @Override
    public Long countTotalRegisterThisMonth() {
        return this.orderRepository.countTotalRegisterThisMonth();
    }

    @Override
    public List<Object[]> sumRevenuePerMonthByYear(String year) {
        return this.orderRepository.sumRevenuePerMonthByYear(year);
    }

    @Override
    public List<Object[]> sumRevenuePerDayByMonth(String monthDate) {
        return this.orderRepository.sumRevenuePerDayByMonth(monthDate);
    }

    @Override
    public List<Object[]> sumRevenuePerSubsPackByMonth(String monthDate) {
        return this.orderRepository.sumRevenuePerSubsPackByMonth(monthDate);
    }

    @Override
    public List<Object[]> countSubscriberStatusPerMonthByYear(String year) {
        List<Object[]> ls = new ArrayList<>();
        ls.addAll(this.orderRepository.calcRenewOrderPerMonthByYear(year));
        ls.addAll(this.orderRepository.calcNotRenewOrderPerMonthByYear(year));

        return ls;
    }

    @Override
    public OrderDto findById(Long id) {
        OrderEntity orderEntity = this.getById(id);
        return OrderDto.toDto(orderEntity);
    }

    @Override
    public Long countTotalCompletedOrderThisMonth() {
        return this.orderRepository.countTotalRegisterThisMonth();
    }

    private void sendOrderInfoToMail(OrderEntity orderEntity) {
        Map<String, Object> context = new HashMap<>();
        context.put("order", orderEntity);
        try {
            this.sendEmailService.sendMail("payments/mail-templates/order_info.html", orderEntity.getUser_id().getEmail(), "Thông tin đặt hàng", context);
        } catch (Exception e) {
            this.logger.error("Send mail failed~");
            e.printStackTrace();
        }
    }

    @Override
    public OrderDto upgradeOrder(UpgradeOrderInput input) {
        OrderEntity originalOrder = this.getById(input.getOriginalOrderId());
        SubscriptionPackEntity subscriptionPack = this.subscriptionPackService.getById(input.getSubscriptionPackId());

        String orderNumber = VnPayConfig.getRandomNumber(10);
        while (this.orderRepository.getByMaDonHang(orderNumber) != null) {
            orderNumber = VnPayConfig.getRandomNumber(10);
        }

        OrderEntity upgradeOrder = OrderEntity.builder()
                .orderType(EOrderType.UPGRADE)
                .maDonHang(orderNumber)
                .subs_pack_id(subscriptionPack)
                .paymentMethod(input.getPaymentMethod())
                .status(EOrderStatus.PENDING_PAYMENT)
                .finalPrice(subscriptionPack.getPrice() - originalOrder.getSubs_pack_id().getPrice())
                .user_id(SecurityUtils.getCurrentUser().getUser())
                .modifiedBy(SecurityUtils.getCurrentUser().getUser())
                .build();

        // task: send mail notify to user
//        this.sendOrderInfoToMail(upgradeOrder);

        this.orderRepository.saveAndFlush(upgradeOrder);
        return OrderDto.toDto(upgradeOrder);

    }


}
