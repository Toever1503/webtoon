package webtoon.main.domains.manga.entities;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tbl_manga_view_count")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@Data
@Builder
public class MangaViewCountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "manga_id")
    private MangaEntity manga;

    @Column(name = "view_count")
    private Integer count;

    @Temporal(TemporalType.DATE)
    @Column(name = "created_at")
    @CreationTimestamp
    private Date createdAt;
}
