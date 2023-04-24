package webtoon.payment.inputs;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import webtoon.account.entities.UserEntity;
import webtoon.payment.entities.OrderEntity;
import webtoon.payment.entities.SubscriptionPackEntity;
import webtoon.payment.enums.EOrderStatus;
import webtoon.payment.enums.EOrderType;
import webtoon.payment.enums.EPaymentMethod;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderInput {
    private Long id;
    private String orderNumber;
    private EOrderStatus status;
    private Long subscriptionPack;
    private Long user_id;
    private EOrderType orderType;
    private EPaymentMethod paymentMethod;

    public static OrderEntity toEntity(OrderInput input) {
        return OrderEntity.builder()
                .id(input.id)
                .paymentMethod(input.paymentMethod)
                .orderType(input.orderType)
                .status(input.status)
                .build();
    }
}
