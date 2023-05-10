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


}
