package webtoon.account.entities;


import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "tbl_role")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleEntity {

    @Id
    @Column(name = "id", unique = true)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name", unique = true)
    private ERoleConstants roleName;
}
