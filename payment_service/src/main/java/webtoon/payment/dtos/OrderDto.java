package webtoon.payment.dtos;

import lombok.*;
import webtoon.payment.entities.OrderEntity;
import webtoon.payment.entities.PaymentEntity;
import webtoon.payment.entities.SubscriptionPackEntity;
import webtoon.payment.enums.EOrderType;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {
    private Long id;
    private Date created_at;
    private Date gioLap;
    private Double finalPrice;
    private int status;
    private String content;
    private String ipAddr;
    private String maDonHang;
    private SubscriptionPackEntity subs_pack_id;

    public static OrderDto toDto(OrderEntity orderEntity) {
        if(orderEntity == null) return null;

        return OrderDto.builder()
                .id(orderEntity.getId())
                .created_at(orderEntity.getCreated_at())
                .gioLap(orderEntity.getGioLap())
                .finalPrice(orderEntity.getFinalPrice())
                .status(orderEntity.getStatus())
                .content(orderEntity.getContent())
                .ipAddr(orderEntity.getIpAddr())
                .maDonHang(orderEntity.getMaDonHang())
                .subs_pack_id(orderEntity.getSubs_pack_id())
                .build();
    }
}
