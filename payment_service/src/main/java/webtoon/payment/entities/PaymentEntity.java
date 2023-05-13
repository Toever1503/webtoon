package webtoon.payment.entities;

import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "tbl_payment")
@Table
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "order_id")
    @OneToOne
    private OrderEntity orderId;

    @Column(name = "trans_no")
    private String transNo;

    @Column(name = "order_info")
    private String orderInfo;

    @Column(name = "card_type")
    private String cardType;

    @Column(name = "pay_money")
    private Double payMoney;

    @Column(name = "bank_code")
    private String bankCode;

    @Column(name = "status")
    private int status;

    @Column(name = "bank_trans_no") // ma giao dich cua ngan hang
    private String bankTranNo;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date paidDate;
}
