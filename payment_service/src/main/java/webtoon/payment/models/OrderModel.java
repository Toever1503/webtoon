package webtoon.payment.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import webtoon.account.entities.UserEntity;
import webtoon.payment.entities.SubscriptionPackEntity;
import webtoon.payment.enums.EOrderStatus;
import webtoon.payment.enums.EOrderType;
import webtoon.payment.enums.EPaymentMethod;

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
    private EOrderType Estatus;
    private EOrderStatus status;
    private String content;
    private String ipAddr;
    private String maDonHang;
    private SubscriptionPackEntity subs_pack_id;
    private UserEntity user_id;
    private EPaymentMethod payment_method;
}
