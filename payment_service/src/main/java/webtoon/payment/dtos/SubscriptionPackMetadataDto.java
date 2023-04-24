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

    public static SubscriptionPackMetadataDto toDto(SubscriptionPackEntity subscriptionPackEntity) {
        if (subscriptionPackEntity == null) return null;
        return SubscriptionPackMetadataDto.builder()
                .id(subscriptionPackEntity.getId())
                .name(subscriptionPackEntity.getName())
                .price(subscriptionPackEntity.getPrice())
                .build();
    }
}
