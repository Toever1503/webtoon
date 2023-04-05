package webtoon.domains.manga.entities;

import javax.persistence.*;

import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tbl_manga_chapter_entity")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MangaChapterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "manga_volume_id")
    private MangaVolumeEntity mangaVolume;
    @ManyToOne
    @JoinColumn(name = "manga_id")
    private MangaEntity manga;

    @Column(name = "chapter_name")
    private String chapterName;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "chapter_index")
    private Integer chapterIndex;

    @Column(name = "required_vip")
    private Boolean requiredVip;

    @OneToMany(mappedBy = "mangaChapter", cascade = CascadeType.ALL)
    @OrderBy("imageIndex asc")
    private List<MangaChapterImageEntity> chapterImages;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date createdAt;

    @Column(name = "modified_at")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date modifiedAt;
}
