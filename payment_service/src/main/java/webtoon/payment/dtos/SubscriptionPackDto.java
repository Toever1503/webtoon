package webtoon.payment.dtos;

import lombok.*;
import webtoon.account.dtos.UserDto;
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
    private Integer monthCount;
    private Integer dayCount;
    private Double price;
    private Date createdAt;
    private Date modifiedAt;

    private UserDto createdBy;
    private UserDto updatedBy;

    public static SubscriptionPackDto toDto(SubscriptionPackEntity subscriptionPackEntity) {
        if (subscriptionPackEntity == null) return null;

        return SubscriptionPackDto.builder()
                .id(subscriptionPackEntity.getId())
                .name(subscriptionPackEntity.getName())
                .monthCount(subscriptionPackEntity.getMonthCount())
                .dayCount(subscriptionPackEntity.getDayCount())
                .price(subscriptionPackEntity.getPrice())
                .createdAt(subscriptionPackEntity.getCreatedAt())
                .modifiedAt(subscriptionPackEntity.getModifiedAt())
                .createdBy(UserDto.toDto(subscriptionPackEntity.getCreatedBy()))
                .updatedBy(UserDto.toDto(subscriptionPackEntity.getUpdatedBy()))
                .build();
    }
}
