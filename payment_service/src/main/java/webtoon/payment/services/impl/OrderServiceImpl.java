package webtoon.payment.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import webtoon.account.configs.security.SecurityUtils;
import webtoon.account.services.IUserService;
import webtoon.payment.dtos.OrderDto;
import webtoon.payment.entities.OrderEntity;
import webtoon.payment.entities.SubscriptionPackEntity;
import webtoon.payment.enums.EOrderType;
import webtoon.payment.inputs.OrderInput;
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
        return this.orderRepository.findById(id).orElseThrow(() -> new CustomHandleException(12));
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

        String orderNumber = UUID.randomUUID().toString();
        while (this.orderRepository.getByMaDonHang(orderNumber) != null) {
            orderNumber = UUID.randomUUID().toString();
        }
        entity.setSubs_pack_id(subscriptionPack);
        entity.setFinalPrice(subscriptionPack.getPrice());


        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, subscriptionPack.getMonthCount());
        entity.setExpiredSubsDate(calendar.getTime());

        entity.setMaDonHang(orderNumber);
        entity.setUser_id(userService.getById(input.getUser_id()));

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


}
