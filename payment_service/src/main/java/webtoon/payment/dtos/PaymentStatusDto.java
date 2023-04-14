package webtoon.payment.dtos;

import lombok.*;
import webtoon.payment.entities.OrderEntity;
import webtoon.payment.entities.PaymentStatusEntity;
import webtoon.payment.enums.EPaymentStatus;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PaymentStatusDto {
    private Long id;
    private OrderEntity orderId;
    private EPaymentStatus status;
    private Date created_at;
    private Date modified_at;

    public static PaymentStatusDto toDto(PaymentStatusEntity paymentStatusEntity) {
        if(paymentStatusEntity == null) return null;

        return PaymentStatusDto.builder()
                .id(paymentStatusEntity.getId())
                .orderId(paymentStatusEntity.getOrderId())
                .status(paymentStatusEntity.getStatus())
                .created_at(paymentStatusEntity.getCreated_at())
                .modified_at(paymentStatusEntity.getModified_at())
                .build();
    }
}
