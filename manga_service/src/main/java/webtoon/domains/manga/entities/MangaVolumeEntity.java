package webtoon.domains.manga.entities;

import java.util.List;
import java.util.Set;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private java.lang.Long id;

    @JoinColumn(name = "manga_id")
    @ManyToOne
    private MangaEntity manga;

    @Column(name = "name")
    private String name;

    @Column(name = "volume_index")
    private Integer volumeIndex;


    @OneToMany(mappedBy = "mangaVolume", cascade = CascadeType.ALL)
    private List<MangaChapterEntity> chapterEntities;
}
