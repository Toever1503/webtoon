package webtoon.main.payment.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import webtoon.main.account.entities.UserEntity;
import webtoon.main.payment.entities.SubscriptionPackEntity;
import webtoon.main.payment.enums.EOrderStatus;
import webtoon.main.payment.enums.EOrderType;
import webtoon.main.payment.enums.EPaymentMethod;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderModel {
    private Long id;
    private Date created_at;
    private Date gioLap;
    private Date expiredSubsDate;
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
