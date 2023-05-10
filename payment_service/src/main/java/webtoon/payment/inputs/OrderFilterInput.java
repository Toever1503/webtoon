package webtoon.payment.inputs;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import webtoon.payment.enums.EOrderStatus;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderFilterInput {
    private String q;
    private List<EOrderStatus> status;
}
