package webtoon.account.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import webtoon.account.enums.EnumSex;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tb_user")
@Getter
@Setter
@Builder
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "sex", nullable = false)
    @Enumerated(EnumType.STRING)
    private EnumSex sex;

    @Column(name = "status", columnDefinition = "boolean default true")
    private Boolean status;

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
