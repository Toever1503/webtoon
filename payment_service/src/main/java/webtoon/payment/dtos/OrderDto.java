package webtoon.payment.dtos;

import lombok.*;
import webtoon.account.dtos.UserDto;
import webtoon.account.entities.UserEntity;
import webtoon.payment.entities.OrderEntity;
import webtoon.payment.entities.SubscriptionPackEntity;
import webtoon.payment.enums.EOrderStatus;
import webtoon.payment.enums.EPaymentMethod;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {
    private Long id;

    private Date expiredSubsDate;
    private Date created_at;
    private Date modifiedAt;
    private Date gioLap;
    private Double finalPrice;
    private EOrderStatus status;
    private String content;
    private String ipAddr;
    private String maDonHang;
    private EPaymentMethod paymentMethod;
    private SubscriptionPackEntity subs_pack_id;
    private UserDto user_id;
    private UserDto modifiedBy;

    public static OrderDto toDto(OrderEntity orderEntity) {
        if(orderEntity == null) return null;

        return OrderDto.builder()
                .id(orderEntity.getId())
                .expiredSubsDate(orderEntity.getExpiredSubsDate())
                .created_at(orderEntity.getCreated_at())
                .modifiedAt(orderEntity.getModifiedAt())
                .gioLap(orderEntity.getGioLap())
                .finalPrice(orderEntity.getFinalPrice())
                .status(orderEntity.getStatus())
                .content(orderEntity.getContent())
                .ipAddr(orderEntity.getIpAddr())
                .maDonHang(orderEntity.getMaDonHang())
                .paymentMethod(orderEntity.getPaymentMethod())
                .subs_pack_id(orderEntity.getSubs_pack_id())
                .user_id(UserDto.toDto(orderEntity.getUser_id()))
                .modifiedBy(UserDto.toDto(orderEntity.getModifiedBy()))
                .build();
    }
}
