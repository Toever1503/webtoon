package webtoon.payment.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import webtoon.payment.enums.EOrderType;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderModel {
    private Long id;
    private Double totalPrice;
    private Double discountPrice;
    private Double finalDiscountPrice;
    private Double finalPrice;
    private String paymentMethod;
    private EOrderType orderType;
    private String note;
    private Date created_at;
    private Date modified_at;
}
