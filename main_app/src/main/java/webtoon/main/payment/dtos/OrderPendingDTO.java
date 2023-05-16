package webtoon.main.payment.dtos;

import lombok.*;
import webtoon.main.account.entities.UserEntity;
import webtoon.main.payment.entities.SubscriptionPackEntity;

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
