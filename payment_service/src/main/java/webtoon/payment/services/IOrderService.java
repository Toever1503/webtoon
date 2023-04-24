package webtoon.payment.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import webtoon.payment.dtos.OrderDto;
import webtoon.payment.entities.OrderEntity;
import webtoon.payment.inputs.OrderInput;
import webtoon.payment.models.OrderModel;

import java.util.List;

public interface IOrderService {
    OrderDto add(OrderModel orderModel);
    OrderDto update(OrderModel orderModel);
//    Page<OrderDto> getAll(Pageable pageable, Specification<OrderEntity> specification);
    List<OrderEntity> getAll();
    OrderEntity getById(Long id);
    OrderEntity getMaDonHang(String maDonHang);

    Page<OrderDto> filter(Pageable pageable, Specification<OrderEntity> finalSpec);
    List<OrderEntity> getByUserId(Long userId);
    Long getIdByMaDonHang(String maDonHang);


    // for api
    OrderDto addNewOrder(OrderInput input);

    OrderDto updateOrder(OrderInput input);

    void deleteById(Long id);
}
