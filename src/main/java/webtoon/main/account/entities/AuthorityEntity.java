package webtoon.main.account.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "tbl_authority")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthorityEntity {

    @Id
    @Column(name = "id", unique = true)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "authority_name", unique = true)
    private EAuthorityConstants authorityName;

}
