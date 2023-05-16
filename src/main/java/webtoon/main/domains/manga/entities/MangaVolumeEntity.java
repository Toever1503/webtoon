package webtoon.main.domains.manga.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;
import webtoon.main.domains.manga.entities.MangaChapterEntity;

@Entity
@Table(name = "manga_volume_entity")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MangaVolumeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "manga_id")
    private MangaEntity manga;

    @Column(name = "name")
    private String name;

    @Column(name = "volume_index")
    private Integer volumeIndex;


    @OneToMany(mappedBy = "mangaVolume", fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<MangaChapterEntity> chapterEntities;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date createdAt;

    @Column(name = "modified_at")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date modifiedAt;
}
