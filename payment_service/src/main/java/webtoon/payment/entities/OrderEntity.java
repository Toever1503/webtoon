package webtoon.payment.entities;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
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
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date created_at;

    @Column
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd-HH:mm:ss")
    private Date gioLap;

    @Column
    private Double finalPrice;

    @Column
    private int status;

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
}
