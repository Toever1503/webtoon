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
@Getter
@Setter
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd-HH:mm:ss")
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

    @Column(name = "month_count")
    private Integer monthCount;

    @Column(name = "day_count")
    private Integer dayCount;

    @Column
    private Double finalPrice;

    @Column
    @Enumerated(EnumType.STRING)
    private EOrderType orderType;

    @Column
    @Enumerated(EnumType.STRING)
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
    @JoinColumn(name = "from_order_id")
    private OrderEntity fromOrder;

    @Override
    public String toString() {
        return "OrderEntity{" +
                "id=" + id +
                ", created_at=" + created_at +
                ", gioLap=" + gioLap +
                ", finalPrice=" + finalPrice +
                ", estatus=" + orderType +
                ", content='" + content + '\'' +
                ", ipAddr='" + ipAddr + '\'' +
                ", maDonHang='" + maDonHang + '\'' +
                ", subs_pack_id=" + subs_pack_id +
                ", user_id=" + user_id +
                '}';
    }
    @ManyToOne
    @JoinColumn(name = "modified_by")
    private UserEntity modifiedBy;

    @Column(name = "deleted_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deletedAt;
}
