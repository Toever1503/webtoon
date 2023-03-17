package webtoon.payment.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import webtoon.payment.entities.OrderEntity;
import webtoon.payment.enums.EPaymentStatus;

import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentStatusModel {
    private Long id;
    private OrderEntity orderId;
    private EPaymentStatus status;
    private Date created_at;
    private Date modified_at;
}
