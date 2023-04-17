package webtoon.payment.entities;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "tbl_subscription_pack")
@Table
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class SubscriptionPackEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "subs_name")
    private String name;

    @Column(name = "day_count")
    private Integer dayCount;

    @Column(name = "month_count")
    private Integer monthCount;

    @Column(name = "original_price", nullable = false)
    private Double originalPrice;

    @Column(name = "discount_price", columnDefinition = "double default 0")
    private Double discountPrice;

    @Deprecated
    @Column
    private Double price;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdAt;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date modifiedAt;

    // private UserEntity created_by
}
