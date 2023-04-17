package webtoon.payment.models;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import webtoon.payment.entities.SubscriptionPackEntity;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionPackModel {
    private Long id;
    private String name;
    private Integer monthCount;

    @JsonSetter
    private Double originalPrice;

    @JsonSetter
    private Double discountPrice;

    public static SubscriptionPackEntity toEntity(SubscriptionPackModel model) {
        if (model == null) return null;

        return SubscriptionPackEntity.builder()
                .id(model.getId())
                .name(model.getName())
                .originalPrice(model.getOriginalPrice())
                .discountPrice(model.getDiscountPrice())
                .monthCount(model.getMonthCount())
                .build();
    }
}
