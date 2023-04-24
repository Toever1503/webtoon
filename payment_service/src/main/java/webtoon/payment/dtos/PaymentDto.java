package webtoon.payment.dtos;

import lombok.*;
import webtoon.payment.entities.OrderEntity;
import webtoon.payment.entities.PaymentEntity;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PaymentDto {
    private Long id;
    private OrderEntity orderId;
    private String transId;
    private String transTrackNumber;
    private Double payMoney;
    private String bank;
    private Integer status;
    private String paymentContent;
    private String payUrl;
    private Date expired_date;

    public static PaymentDto toDto(PaymentEntity paymentEntity) {
        if(paymentEntity == null) return null;

        return PaymentDto.builder()
                .id(paymentEntity.getId())
                .orderId(paymentEntity.getOrderId())
                .transId(paymentEntity.getTransId())
                .transTrackNumber(paymentEntity.getTransTrackNumber())
                .payMoney(paymentEntity.getPayMoney())
                .bank(paymentEntity.getBank())
                .paymentContent(paymentEntity.getPaymentContent())
                .payUrl(paymentEntity.getPayUrl())
                .expired_date(paymentEntity.getExpired_date())
                .build();
    }
}
