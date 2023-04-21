package webtoon.payment.entities;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
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
    private Date created_at;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd-HH:mm:ss")
    private Date gioLap;

    @Column
    private Double finalPrice;

    @Column
    @Enumerated(EnumType.STRING)
    private EOrderType estatus;

    @Column
    private String content;

    @Column
    private String ipAddr;

    @Column
    private String maDonHang;

    @JoinColumn(name ="subs_pack_id")
    @OneToOne
    private SubscriptionPackEntity subs_pack_id;

    @JoinColumn(name ="user_id")
    @ManyToOne
    private UserEntity user_id;

    @Column
    private String payment_method;

    @Override
    public String toString() {
        return "OrderEntity{" +
                "id=" + id +
                ", created_at=" + created_at +
                ", gioLap=" + gioLap +
                ", finalPrice=" + finalPrice +
                ", estatus=" + estatus +
                ", content='" + content + '\'' +
                ", ipAddr='" + ipAddr + '\'' +
                ", maDonHang='" + maDonHang + '\'' +
                ", subs_pack_id=" + subs_pack_id +
                ", user_id=" + user_id +
                ", payment_method='" + payment_method + '\'' +
                '}';
    }
}
