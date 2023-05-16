package webtoon.main.payment.dtos;

import lombok.*;
import webtoon.main.account.dtos.UserDto;
import webtoon.main.payment.entities.OrderEntity;
import webtoon.main.payment.entities.SubscriptionPackEntity;
import webtoon.main.payment.enums.EOrderStatus;
import webtoon.main.payment.enums.EOrderType;
import webtoon.main.payment.enums.EPaymentMethod;

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
    private Double finalPrice;
    private EOrderType orderType;
    private EOrderStatus status;
    private String maDonHang;
    private EPaymentMethod paymentMethod;
    private SubscriptionPackEntity subs_pack_id;
    private UserDto user_id;
    private UserDto modifiedBy;
    private boolean hasUpgradingOrder;

    private PaymentDto paymentDto;

    public static OrderDto toDto(OrderEntity orderEntity) {
        if (orderEntity == null) return null;

        boolean hasUpgradingOrder = false;
//        if (orderEntity.getOtherOrders() != null) {
//
//            Optional<OrderEntity> upgradeOrder = orderEntity.getOtherOrders().stream()
//                    .filter(order -> order.getOrderType().equals(EOrderType.UPGRADE) && order.getDeletedAt() == null)
//                    .filter(order -> order.getStatus().equals(EOrderStatus.PENDING_PAYMENT) || order.getStatus().equals(EOrderStatus.COMPLETED))
//                    .findFirst();
//            if (upgradeOrder.isPresent())
//                hasUpgradingOrder = true;
//        }

        return OrderDto.builder()
                .id(orderEntity.getId())
                .created_at(orderEntity.getCreated_at())
                .modifiedAt(orderEntity.getModifiedAt())
                .finalPrice(orderEntity.getFinalPrice())
                .orderType(orderEntity.getOrderType())
                .status(orderEntity.getStatus())
                .maDonHang(orderEntity.getMaDonHang())
                .paymentMethod(orderEntity.getPaymentMethod())
                .subs_pack_id(orderEntity.getSubs_pack_id())
                .user_id(UserDto.toDto(orderEntity.getUser_id()))
                .modifiedBy(UserDto.toDto(orderEntity.getModifiedBy()))
                .hasUpgradingOrder(hasUpgradingOrder)
                .paymentDto(PaymentDto.toDto(orderEntity.getPaymentEntity()))
                .build();
    }
}
