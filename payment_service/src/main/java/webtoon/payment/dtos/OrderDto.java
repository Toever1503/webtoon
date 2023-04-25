package webtoon.payment.dtos;

import lombok.*;
import webtoon.account.dtos.UserDto;
import webtoon.payment.entities.OrderEntity;
import webtoon.payment.entities.SubscriptionPackEntity;
import webtoon.payment.enums.EOrderType;
import webtoon.payment.enums.EOrderStatus;
import webtoon.payment.enums.EPaymentMethod;

import java.util.Date;
import java.util.Optional;

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
    private EOrderType orderType;
    private EOrderStatus status;
    private String content;
    private String ipAddr;
    private String maDonHang;
    private EPaymentMethod paymentMethod;
    private SubscriptionPackEntity subs_pack_id;
    private String payment_method;
    private UserDto user_id;
    private UserDto modifiedBy;

    private OrderDto fromOrder;
    private boolean hasUpgradingOrder;

    public static OrderDto toDto(OrderEntity orderEntity) {
        if (orderEntity == null) return null;

        boolean hasUpgradingOrder = false;
        if (orderEntity.getOtherOrders() != null) {

            Optional<OrderEntity> upgradeOrder = orderEntity.getOtherOrders().stream()
                    .filter(order -> order.getOrderType().equals(EOrderType.UPGRADE) && order.getDeletedAt() == null)
                    .filter(order -> order.getStatus().equals(EOrderStatus.PENDING_PAYMENT) || order.getStatus().equals(EOrderStatus.COMPLETED))
                    .findFirst();
            if (upgradeOrder.isPresent())
                hasUpgradingOrder = true;
        }

        return OrderDto.builder()
                .id(orderEntity.getId())
                .expiredSubsDate(orderEntity.getExpiredSubsDate())
                .created_at(orderEntity.getCreated_at())
                .modifiedAt(orderEntity.getModifiedAt())
                .gioLap(orderEntity.getGioLap())
                .finalPrice(orderEntity.getFinalPrice())
                .orderType(orderEntity.getOrderType())
                .status(orderEntity.getStatus())
                .content(orderEntity.getContent())
                .ipAddr(orderEntity.getIpAddr())
                .maDonHang(orderEntity.getMaDonHang())
                .paymentMethod(orderEntity.getPaymentMethod())
                .subs_pack_id(orderEntity.getSubs_pack_id())
                .user_id(UserDto.toDto(orderEntity.getUser_id()))
                .modifiedBy(UserDto.toDto(orderEntity.getModifiedBy()))
                .hasUpgradingOrder(hasUpgradingOrder)
                .fromOrder(OrderDto.toDto(orderEntity.getFromOrder()))
                .build();
    }
}
