package webtoon.domains.manga.entities;

import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import webtoon.account.entities.UserEntity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tbl_read_history")
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@Setter
@Builder
public class ReadHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "manga_id")
    private Long mangaEntity;


    @Column(name = "chapter_id")
    private Long chapterEntity;

    @JoinColumn(name = "created_by")
    @ManyToOne
    private UserEntity createdBy;

    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date createdDate;

}
