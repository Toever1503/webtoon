package webtoon.payment.dtos;

import lombok.*;
import webtoon.account.entities.UserEntity;
import webtoon.payment.entities.SubscriptionPackEntity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderPendingDTO {
    private Long id;
    private UserEntity user_id;
    private String maDonHang;
    private SubscriptionPackEntity subs_pack_id;

    @Override
    public String toString() {
        return "OrderPendingDTO{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", maDonHang='" + maDonHang + '\'' +
                '}';
    }
}
