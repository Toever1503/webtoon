package webtoon.payment.dtos;

import lombok.*;
import webtoon.payment.entities.SubscriptionPackEntity;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SubscriptionPackDto {
    private Long id;
    private String name;
    private String desc;
    private Integer dayCount;
    private Double originalPrice;
    private Double discountPrice;
    private Double price;
    private long discountPercent;
    private Date createdAt;
    private Date modifiedAt;

    public static SubscriptionPackDto toDto(SubscriptionPackEntity subscriptionPackEntity) {
        if (subscriptionPackEntity == null) return null;

        long discountPercent = 0;
        if (subscriptionPackEntity.getDiscountPrice() != null)
            discountPercent = Math.round(subscriptionPackEntity.getDiscountPrice() / subscriptionPackEntity.getOriginalPrice() * 100);
        return SubscriptionPackDto.builder()
                .id(subscriptionPackEntity.getId())
                .name(subscriptionPackEntity.getName())
                .dayCount(subscriptionPackEntity.getDayCount())
                .originalPrice(subscriptionPackEntity.getOriginalPrice())
                .price(subscriptionPackEntity.getPrice())
                .discountPrice(subscriptionPackEntity.getDiscountPrice())
                .discountPercent(discountPercent)
                .createdAt(subscriptionPackEntity.getCreatedAt())
                .modifiedAt(subscriptionPackEntity.getModifiedAt())
                .build();
    }
}
