package webtoon.main.payment.inputs;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import webtoon.main.payment.enums.EOrderStatus;
import webtoon.main.payment.enums.EPaymentMethod;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderFilterInput {
    private String q;
    private List<EOrderStatus> status;

    private EPaymentMethod paymentMethod;
    private Date fromDate;
    private Date toDate;
}
