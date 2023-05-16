package webtoon.account.entities;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import webtoon.account.enums.EAccountType;
import webtoon.account.enums.ESex;
import webtoon.account.enums.EStatus;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "tbl_user"
//        , uniqueConstraints = {
//                @UniqueConstraint(columnNames = { "email", "account_type" }) }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "full_name")
    private String fullName;
    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "phone")
    private String phone;
    @Column(name = "account_type")
    @Enumerated(EnumType.STRING)
    private EAccountType accountType;

    @Column(name = "password")
    private String password;

    @Column(name = "sex")
    @Enumerated(EnumType.STRING)
    private ESex sex;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EStatus status;

    @Column(name = "has_blocked")
    private Boolean hasBlocked;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    @CreationTimestamp
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_at")
    @UpdateTimestamp
    private Date modifiedAt;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private RoleEntity role;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "tbl_user_authority",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id"))
    private Set<AuthorityEntity> authorities;


    @Temporal(TemporalType.DATE)
    @Column(name = "deleted_at")
    private Date deletedAt;
    // count date

    @Temporal(TemporalType.DATE)
    @Column(name = "trial_registered_date")
    private Date trialRegisteredDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "can_read_until_date")
    private Date canReadUntilDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "first_bought_subs_date")
    private Date firstBoughtSubsDate;

    @Column(name = "current_used_subs_id")
    private Long currentUsedSubsId;

    @Column(name = "current_expired_subs_id")
    private Long currentExpiredSubsId;

    @Column(name = "has_send_renewal_email", columnDefinition = "boolean default false")
    private Boolean hasSendRenewalEmail;
}
