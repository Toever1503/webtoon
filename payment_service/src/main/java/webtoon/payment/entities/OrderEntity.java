package webtoon.payment.entities;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;
import webtoon.payment.enums.EOrderStatus;
import webtoon.payment.enums.EPaymentMethod;
import webtoon.account.entities.UserEntity;
import webtoon.payment.enums.EOrderType;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "tbl_order")
@Table
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "final_price")
    private Double finalPrice;

    @Column(name = "order_type")
    @Enumerated(EnumType.STRING)
    private EOrderType orderType;

    @Column
    @Enumerated(EnumType.STRING)
    private EOrderStatus status;

    @Column(unique = true)
    private String maDonHang;

     @Column
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd-HH:mm:ss")
    @CreationTimestamp
    private Date created_at;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date modifiedAt;

    @Enumerated(EnumType.STRING)
    @Column
    private EPaymentMethod paymentMethod;

    @JoinColumn(name ="subs_pack_id")
    @OneToOne
    private SubscriptionPackEntity subs_pack_id;

    @JoinColumn(name ="user_id")
    @ManyToOne
    private UserEntity user_id;

    @ManyToOne
    @JoinColumn(name = "modified_by")
    private UserEntity modifiedBy;

    @Column(name = "deleted_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deletedAt;


    @OneToOne(mappedBy = "orderId")
    private PaymentEntity paymentEntity;

}
