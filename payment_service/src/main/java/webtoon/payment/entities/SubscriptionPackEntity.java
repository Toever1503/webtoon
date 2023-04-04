package webtoon.payment.entities;

import lombok.*;
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
    @Column(name = "description")
    private String desc;
    @Column(name = "day_count")
    private Integer dayCount;
    @Column(name = "price")
    private Double price;
    @Column
    @Temporal(TemporalType.DATE)
    private Date createdAt;

    @Column
    @Temporal(TemporalType.DATE)
    @UpdateTimestamp
    private Date modifiedAt;

    // private UserEntity created_by
}
