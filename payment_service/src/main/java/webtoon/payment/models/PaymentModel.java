package webtoon.payment.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import webtoon.payment.entities.OrderEntity;

import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentModel {
    private Long id;
    private OrderEntity orderId;
    private String transId;
    private String transTrackNumber;
    private Double payMoney;
    private String bank;
    private String paymentContent;
    private String payUrl;
    private Date expired_date;
}
