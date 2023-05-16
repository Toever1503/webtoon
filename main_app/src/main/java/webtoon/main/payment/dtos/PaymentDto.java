package webtoon.main.payment.dtos;

import lombok.*;
import webtoon.main.payment.entities.PaymentEntity;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PaymentDto {
    private Long id;

    private String transNo;

    private String orderInfo;

    private String cardType;

    private Double payMoney;

    private String bankCode;

    private int status;

    private String bankTranNo;

    private Date paidDate;

    public static PaymentDto toDto(PaymentEntity paymentEntity) {
        if (paymentEntity == null) return null;

        return PaymentDto.builder()
                .id(paymentEntity.getId())
                .transNo(paymentEntity.getTransNo())
                .orderInfo(paymentEntity.getOrderInfo())
                .cardType(paymentEntity.getCardType())
                .payMoney(paymentEntity.getPayMoney())
                .bankCode(paymentEntity.getBankCode())
                .status(paymentEntity.getStatus())
                .bankTranNo(paymentEntity.getBankTranNo())
                .paidDate(paymentEntity.getPaidDate())
                .build();


    }
}
