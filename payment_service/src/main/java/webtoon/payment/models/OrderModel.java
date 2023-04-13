package webtoon.payment.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import webtoon.payment.entities.PaymentEntity;
import webtoon.payment.entities.SubscriptionPackEntity;
import webtoon.payment.enums.EOrderType;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderModel {
    private Long id;
    private Date created_at;
    private Date gioLap;
    private Double finalPrice;
    private int status;
    private String content;
    private String ipAddr;
    private String maDonHang;
    private SubscriptionPackEntity subs_pack_id;
}
