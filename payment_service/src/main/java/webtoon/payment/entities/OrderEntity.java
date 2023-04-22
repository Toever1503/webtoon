package webtoon.payment.entities;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;
import webtoon.payment.enums.EOrderStatus;
import webtoon.payment.enums.EPaymentMethod;
import webtoon.account.entities.UserEntity;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "tbl_order")
@Table
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date created_at;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date modifiedAt;

    @Column
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd-HH:mm:ss")
    private Date gioLap;

    @Column(name = "expired_subs_date")
    private Date expiredSubsDate;

    @Column
    private Double finalPrice;

    @Column
    private EOrderStatus status;

    @Column
    private String content;

    @Column
    private String ipAddr;

    @Column
    private String maDonHang;

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
}
