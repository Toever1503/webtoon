package webtoon.payment.services;

import webtoon.payment.dtos.OrderDto;
import webtoon.payment.models.OrderModel;

public interface IOrderService {
    OrderDto add(OrderModel orderModel);
    OrderDto update(OrderModel orderModel);

}
