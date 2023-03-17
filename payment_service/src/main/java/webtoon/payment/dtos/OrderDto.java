package webtoon.payment.dtos;

import lombok.*;
import webtoon.payment.entities.OrderEntity;
import webtoon.payment.enums.EOrderType;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {
    private Long id;
    private Double totalPrice;
    private Double discountPrice;
    private Double finalDiscountPrice;
    private Double finalPrice;
    private String paymentMethod;
    private EOrderType orderType;
    private String note;
    private Date created_at;
    private Date modified_at;

    public static OrderDto toDto(OrderEntity orderEntity) {
        if (orderEntity == null) return null;
        return OrderDto.builder()
                .id(orderEntity.getId())
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
}
