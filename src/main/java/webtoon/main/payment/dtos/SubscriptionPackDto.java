package webtoon.main.payment.dtos;

import lombok.*;
import webtoon.main.account.dtos.UserDto;
import webtoon.main.payment.entities.SubscriptionPackEntity;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SubscriptionPackDto {
    private Long id;
    private String name;
    private String subsCode;
    private Integer monthCount;
    private Double price;
    private Date modifiedAt;

    private UserDto updatedBy;
    private String description;

    public static SubscriptionPackDto toDto(SubscriptionPackEntity subscriptionPackEntity) {
        if (subscriptionPackEntity == null) return null;

        return SubscriptionPackDto.builder()
                .id(subscriptionPackEntity.getId())
                .subsCode(subscriptionPackEntity.getSubsCode())
                .monthCount(subscriptionPackEntity.getMonthCount())
                .name(subscriptionPackEntity.getName())
                .price(subscriptionPackEntity.getPrice())
                .modifiedAt(subscriptionPackEntity.getModifiedAt())
                .updatedBy(UserDto.toDto(subscriptionPackEntity.getUpdatedBy()))
                .description(subscriptionPackEntity.getDescription())
                .build();
    }
}
