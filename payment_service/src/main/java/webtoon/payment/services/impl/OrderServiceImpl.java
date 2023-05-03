package webtoon.payment.services.impl;

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
import webtoon.payment.services.ISubscriptionPackService;
import webtoon.utils.exception.CustomHandleException;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private IOrderRepository orderRepository;

    @Autowired
    private ISubscriptionPackService subscriptionPackService;

    @Autowired
    private IUserService userService;

    @Override
    public OrderDto add(OrderModel orderModel) {
        OrderEntity orderEntity = OrderEntity.builder()
                .created_at(orderModel.getCreated_at())
                .gioLap(orderModel.getGioLap())
                .expiredSubsDate(orderModel.getExpiredSubsDate())
                .finalPrice(orderModel.getFinalPrice())
                .orderType(orderModel.getEstatus())
                .status(orderModel.getStatus())
                .content(orderModel.getContent())
                .ipAddr(orderModel.getIpAddr())
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
        orderEntity.setGioLap(orderModel.getGioLap());
        orderEntity.setExpiredSubsDate(orderModel.getExpiredSubsDate());
        orderEntity.setFinalPrice(orderModel.getFinalPrice());
        orderEntity.setOrderType(orderModel.getEstatus());
        orderEntity.setStatus(orderModel.getStatus());
        orderEntity.setContent(orderModel.getContent());
        orderEntity.setIpAddr(orderModel.getIpAddr());
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


        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, subscriptionPack.getMonthCount());
        entity.setExpiredSubsDate(calendar.getTime());

        entity.setMaDonHang(orderNumber);
        entity.setUser_id(userService.getById(input.getUser_id()));
        entity.setMonthCount(subscriptionPack.getMonthCount());
        entity.setDayCount(subscriptionPack.getDayCount());

        entity.setModifiedBy(SecurityUtils.getCurrentUser().getUser());
        this.orderRepository.saveAndFlush(entity);

        return OrderDto.toDto(entity);
    }

    @Override
    public OrderDto updateOrder(OrderInput input) {
        OrderEntity entity = this.getById(input.getId());


        if (!entity.getSubs_pack_id().getId().equals(input.getSubscriptionPack())) {
            SubscriptionPackEntity subscriptionPack = this.subscriptionPackService.getById(input.getSubscriptionPack());

            entity.setSubs_pack_id(subscriptionPack);
            entity.setFinalPrice(subscriptionPack.getPrice());

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, subscriptionPack.getMonthCount());
            entity.setExpiredSubsDate(calendar.getTime());
            entity.setMonthCount(subscriptionPack.getMonthCount());
            entity.setDayCount(subscriptionPack.getDayCount());
        }

        entity.setOrderType(input.getOrderType());
        entity.setStatus(input.getStatus());
        entity.setPaymentMethod(input.getPaymentMethod());

        entity.setModifiedBy(SecurityUtils.getCurrentUser().getUser());

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
        entity.setMonthCount(subscriptionPackEntity.getMonthCount());
        entity.setDayCount(subscriptionPackEntity.getDayCount());
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
        if(!orderEntity.getUser_id().getId().equals(SecurityUtils.getCurrentUser().getUser().getId()))
            throw new CustomHandleException(42);
        orderEntity.setStatus(EOrderStatus.CANCELED);
        this.orderRepository.saveAndFlush(orderEntity);
    }

    @Override
    public void returnOrder(Long id) {
        OrderEntity orderEntity = this.getById(id);
        if(!orderEntity.getUser_id().getId().equals(SecurityUtils.getCurrentUser().getUser().getId()))
            throw new CustomHandleException(43);
        orderEntity.setStatus(EOrderStatus.REFUNDING);
        this.orderRepository.saveAndFlush(orderEntity);
    }

    @Override
    public OrderDto upgradeOrder(UpgradeOrderInput input) {
        OrderEntity originalOrder = this.getById(input.getOriginalOrderId());
        SubscriptionPackEntity subscriptionPack = this.subscriptionPackService.getById(input.getSubscriptionPackId());

        String orderNumber = UUID.randomUUID().toString();
        while (this.orderRepository.getByMaDonHang(orderNumber) != null) {
            orderNumber = UUID.randomUUID().toString();
        }

        OrderEntity upgradeOrder = OrderEntity.builder()
                .fromOrder(originalOrder)
                .orderType(EOrderType.UPGRADE)
                .maDonHang(orderNumber)
                .subs_pack_id(subscriptionPack)
                .paymentMethod(input.getPaymentMethod())
                .status(EOrderStatus.PENDING_PAYMENT)
                .finalPrice(subscriptionPack.getPrice() - originalOrder.getSubs_pack_id().getPrice())
                .content("Upgrade order")
                .user_id(SecurityUtils.getCurrentUser().getUser())
                .modifiedBy(SecurityUtils.getCurrentUser().getUser())
                .monthCount(subscriptionPack.getMonthCount() - originalOrder.getSubs_pack_id().getMonthCount())
                .dayCount(subscriptionPack.getDayCount() - originalOrder.getSubs_pack_id().getDayCount())
                .build();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, upgradeOrder.getMonthCount());
        upgradeOrder.setExpiredSubsDate(calendar.getTime());

        this.orderRepository.saveAndFlush(upgradeOrder);
        return OrderDto.toDto(upgradeOrder);

    }


}
