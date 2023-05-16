package webtoon.main.payment.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import webtoon.main.payment.entities.SubscriptionPackEntity;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionPackModel {
    private Long id;
    private String name;
    private String description;
    private int monthCount;
    private Double price;


    public static SubscriptionPackEntity toEntity(SubscriptionPackModel model) {
        if (model == null) return null;

        return SubscriptionPackEntity.builder()
                .id(model.getId())
                .name(model.getName())
                .description(model.getDescription())
                .price(model.getPrice())
                .monthCount(model.getMonthCount())
                .build();
    }
}
