package webtoon.payment.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import webtoon.payment.dtos.OrderDto;
import webtoon.payment.entities.OrderEntity;
import webtoon.payment.models.OrderModel;
import webtoon.payment.repositories.IOrderRepository;
import webtoon.payment.services.IOrderService;

@Service
@Transactional
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private IOrderRepository orderRepository;

    @Override
    public OrderDto add(OrderModel orderModel) {
        OrderEntity orderEntity = OrderEntity.builder()
                .totalPrice(orderModel.getTotalPrice())
                .discountPrice(orderModel.getDiscountPrice())
                .finalDiscountPrice(orderModel.getFinalDiscountPrice())
                .finalPrice(orderModel.getFinalPrice())
                .paymentMethod(orderModel.getPaymentMethod())
                .orderType(orderModel.getOrderType())
                .note(orderModel.getNote())
                .created_at(orderModel.getCreated_at())
                .modified_at(orderModel.getModified_at())
                .build();
        this.orderRepository.saveAndFlush(orderEntity);
        return OrderDto.builder()
                .totalPrice(orderEntity.getTotalPrice())
                .discountPrice(orderEntity.getDiscountPrice())
                .finalDiscountPrice(orderEntity.getFinalDiscountPrice())
                .finalPrice(orderEntity.getFinalPrice())
                .paymentMethod(orderEntity.getPaymentMethod())
                .orderType(orderEntity.getOrderType())
                .note(orderEntity.getNote())
                .created_at(orderEntity.getCreated_at())
                .modified_at(orderEntity.getModified_at())
                .build();
    }

    @Override
    public OrderDto update(OrderModel orderModel) {
        OrderEntity orderEntity = this.getById(orderModel.getId());
        orderEntity.setTotalPrice(orderModel.getTotalPrice());
        orderEntity.setDiscountPrice(orderModel.getDiscountPrice());
        orderEntity.setFinalDiscountPrice(orderModel.getFinalDiscountPrice());
        orderEntity.setFinalPrice(orderModel.getFinalPrice());
        orderEntity.setPaymentMethod(orderModel.getPaymentMethod());
        orderEntity.setOrderType(orderModel.getOrderType());
        orderEntity.setNote(orderModel.getNote());
        orderEntity.setCreated_at(orderModel.getCreated_at());
        orderEntity.setModified_at(orderModel.getModified_at());
        this.orderRepository.saveAndFlush(orderEntity);
        return OrderDto.builder()
                .totalPrice(orderEntity.getTotalPrice())
                .discountPrice(orderEntity.getDiscountPrice())
                .finalDiscountPrice(orderEntity.getFinalDiscountPrice())
                .finalPrice(orderEntity.getFinalPrice())
                .paymentMethod(orderEntity.getPaymentMethod())
                .orderType(orderEntity.getOrderType())
                .note(orderEntity.getNote())
                .created_at(orderEntity.getCreated_at())
                .modified_at(orderEntity.getModified_at())
                .build();
    }

    public OrderEntity getById(Long id){
        return this.orderRepository.findById(id).get();
    }
}
