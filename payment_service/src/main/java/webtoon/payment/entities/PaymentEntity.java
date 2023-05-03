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

    @Column(name = "trans_id")
    private String transId;

    @Column(name = "trans_track_number")
    private String transTrackNumber;

    @Column(name = "pay_money")
    private Double payMoney;

    @Column(name = "bank")
    private String bank;

    @Column(name = "status")
    private int status;

    @Column(name = "payment_content")
    private String paymentContent;


    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date expired_date;
}
