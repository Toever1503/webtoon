package webtoon.payment.inputs;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import webtoon.payment.enums.EOrderStatus;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderFilterInput {
    private String q;
    private EOrderStatus status;
}
