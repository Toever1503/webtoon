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
    private String subsCode;
    private Integer monthCount;
    private Double price;
    private Date modifiedAt;

    private UserDto updatedBy;
    private Boolean status;

    public static SubscriptionPackDto toDto(SubscriptionPackEntity subscriptionPackEntity) {
        if (subscriptionPackEntity == null) return null;

        return SubscriptionPackDto.builder()
                .id(subscriptionPackEntity.getId())
                .subsCode(subscriptionPackEntity.getSubsCode())
                .name(subscriptionPackEntity.getName())
                .price(subscriptionPackEntity.getPrice())
                .modifiedAt(subscriptionPackEntity.getModifiedAt())
                .updatedBy(UserDto.toDto(subscriptionPackEntity.getUpdatedBy()))
                .status(subscriptionPackEntity.getStatus())
                .build();
    }
}
