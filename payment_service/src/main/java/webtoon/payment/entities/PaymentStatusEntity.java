package webtoon.payment.entities;

import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import webtoon.payment.enums.EPaymentStatus;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "tbl_payment_status")
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PaymentStatusEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "order_id")
    @ManyToOne
    private OrderEntity orderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EPaymentStatus status;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date created_at;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date modified_at;

//    private User created_by;

}
