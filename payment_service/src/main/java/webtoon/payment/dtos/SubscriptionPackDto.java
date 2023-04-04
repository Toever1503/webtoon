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
    private Double price;
    private Date createdAt;
    private Date modifiedAt;

    public static SubscriptionPackDto toDto(SubscriptionPackEntity subscriptionPackEntity) {
        if(subscriptionPackEntity == null) return null;

        return SubscriptionPackDto.builder()
                .id(subscriptionPackEntity.getId())
                .name(subscriptionPackEntity.getName())
                .desc(subscriptionPackEntity.getDesc())
                .dayCount(subscriptionPackEntity.getDayCount())
                .price(subscriptionPackEntity.getPrice())
                .createdAt(subscriptionPackEntity.getCreatedAt())
                .modifiedAt(subscriptionPackEntity.getModifiedAt())
                .build();
    }
}
