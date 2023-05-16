package webtoon.main.payment.inputs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import webtoon.main.payment.enums.EPaymentMethod;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UpgradeOrderInput {
    private Long id;
    private Long originalOrderId;
    private Long subscriptionPackId;
    private EPaymentMethod paymentMethod;
}
