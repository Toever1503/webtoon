package webtoon.payment.services;

import webtoon.payment.dtos.OrderDto;
import webtoon.payment.entities.OrderEntity;
import webtoon.payment.models.OrderModel;

import java.util.List;

public interface IOrderService {
    OrderDto add(OrderModel orderModel);
    OrderDto update(OrderModel orderModel);
//    Page<OrderDto> getAll(Pageable pageable, Specification<OrderEntity> specification);
    List<OrderEntity> getAll();
    OrderEntity getById(Long id);
    OrderEntity getMaDonHang(String maDonHang);
    List<OrderEntity> getByUserId(Long userId);
    Long getIdByMaDonHang(String maDonHang);
}
