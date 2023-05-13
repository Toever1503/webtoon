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

    @Column(name = "subs_code")
    private String subsCode;

    @Column(name = "description")
    private String description;

    @Column(name = "subs_name")
    private String name;

    @Column(name = "month_count")
    private Integer monthCount;

    @Column
    private Double price;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date modifiedAt;

    @Column(name = "deleted_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deletedAt;

    @ManyToOne
    @JoinColumn(name = "updated_by")
    private UserEntity updatedBy;

    @Column(columnDefinition = "boolean default true")
    private Boolean status;

}
