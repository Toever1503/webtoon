package webtoon.comment.entities;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import webtoon.account.enums.EAccountType;
import webtoon.account.enums.ESex;
import webtoon.account.enums.EStatus;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tbl_user")
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

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "email", unique = true)
    private String email;

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

    @Column(name = "number_of_failed_signin")
    private Integer numberOfFailedSignIn;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    @CreatedDate
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_at")
    @LastModifiedDate
    private Date modifiedAt;
}
