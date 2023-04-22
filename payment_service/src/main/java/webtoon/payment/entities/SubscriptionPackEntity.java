package webtoon.payment.entities;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;
import webtoon.account.entities.UserEntity;

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

    @ManyToOne
    @JoinColumn(name = "created_by")
    private UserEntity createdBy;

    @ManyToOne
    @JoinColumn(name = "updated_by")
    private UserEntity updatedBy;

}
