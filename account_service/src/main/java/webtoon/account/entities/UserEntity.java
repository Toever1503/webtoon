package webtoon.account.entities;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import webtoon.account.enums.EnumSex;
import webtoon.account.enums.EnumStatus;

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

    @Column(name = "username")
    private String username;

    @Column(name = "full_name")
    private String fullName;

    @OneToOne
    @JoinColumn(name = "avatar")
    private FileEntity avatar;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "sex")
    @Enumerated(EnumType.STRING)
    private EnumSex sex;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EnumStatus status;

    @Column(name = "has_blocked", columnDefinition = "boolean default false")
    private Boolean hasBlocked;

    @Column(name = "number of failed sign in", columnDefinition = "int default 0")
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
