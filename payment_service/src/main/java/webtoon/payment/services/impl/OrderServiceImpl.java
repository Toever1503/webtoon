package webtoon.payment.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import webtoon.payment.dtos.OrderDto;
import webtoon.payment.entities.OrderEntity;
import webtoon.payment.models.OrderModel;
import webtoon.payment.repositories.IOrderRepository;
import webtoon.payment.services.IOrderService;

import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private IOrderRepository orderRepository;

    @Override
    public OrderDto add(OrderModel orderModel) {
      OrderEntity orderEntity = OrderEntity.builder()
              .created_at(orderModel.getCreated_at())
              .gioLap(orderModel.getGioLap())
              .finalPrice(orderModel.getFinalPrice())
              .estatus(orderModel.getEstatus())
              .content(orderModel.getContent())
              .ipAddr(orderModel.getIpAddr())
              .maDonHang(orderModel.getMaDonHang())
                .subs_pack_id(orderModel.getSubs_pack_id())
              .user_id(orderModel.getUser_id())
              .payment_method(orderModel.getPayment_method())
              .build();
      this.orderRepository.saveAndFlush(orderEntity);
      return OrderDto.builder()
              .created_at(orderEntity.getCreated_at())
              .gioLap(orderEntity.getGioLap())
              .finalPrice(orderEntity.getFinalPrice())
              .estatus(orderEntity.getEstatus())
              .content(orderEntity.getContent())
              .ipAddr(orderEntity.getIpAddr())
              .maDonHang(orderEntity.getMaDonHang())
              .subs_pack_id(orderEntity.getSubs_pack_id())
              .user_id(orderEntity.getUser_id())
              .payment_method(orderEntity.getPayment_method())
              .build();
    }
//
    @Override
    public OrderDto update(OrderModel orderModel) {
        OrderEntity orderEntity = this.getById(orderModel.getId());
        orderEntity.setCreated_at(orderModel.getCreated_at());
        orderEntity.setGioLap(orderModel.getGioLap());
        orderEntity.setFinalPrice(orderModel.getFinalPrice());
        orderEntity.setEstatus(orderModel.getEstatus());
        orderEntity.setContent(orderModel.getContent());
        orderEntity.setIpAddr(orderModel.getIpAddr());
        orderEntity.setMaDonHang(orderModel.getMaDonHang());
        orderEntity.setSubs_pack_id(orderModel.getSubs_pack_id());
        orderEntity.setUser_id(orderModel.getUser_id());
        orderEntity.setPayment_method(orderModel.getPayment_method());
        this.orderRepository.saveAndFlush(orderEntity);
        return OrderDto.builder()
                .created_at(orderEntity.getCreated_at())
                .gioLap(orderEntity.getGioLap())
                .finalPrice(orderEntity.getFinalPrice())
                .estatus(orderEntity.getEstatus())
                .content(orderEntity.getContent())
                .ipAddr(orderEntity.getIpAddr())
                .maDonHang(orderEntity.getMaDonHang())
                .subs_pack_id(orderEntity.getSubs_pack_id())
                .user_id(orderEntity.getUser_id())
                .payment_method(orderEntity.getPayment_method())
                .build();
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
    public OrderEntity getById(Long id){
        return this.orderRepository.findById(id).get();
    }

    @Override
    public OrderEntity getMaDonHang(String maDonHang) {
        return this.orderRepository.getByMaDonHang(maDonHang);
    }

    @Override
    public List<OrderEntity> getByUserId(Long userId) {
        return orderRepository.getByUserId(userId);
    }

    @Override
    public Long getIdByMaDonHang(String maDonHang) {
        return orderRepository.getIdByMaDonHang(maDonHang);
    }


}
