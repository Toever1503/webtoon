package webtoon.payment.dtos;

import lombok.*;
import webtoon.payment.entities.SubscriptionPackEntity;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SubscriptionPackMetadataDto {
    private Long id;
    private String name;

    private Double price;

    private int monthCount;

    public static SubscriptionPackMetadataDto toDto(SubscriptionPackEntity subscriptionPackEntity) {
        if (subscriptionPackEntity == null) return null;
        return SubscriptionPackMetadataDto.builder()
                .id(subscriptionPackEntity.getId())
                .name(subscriptionPackEntity.getName())
                .price(subscriptionPackEntity.getPrice())
                .monthCount(subscriptionPackEntity.getMonthCount())
                .build();
    }
}
