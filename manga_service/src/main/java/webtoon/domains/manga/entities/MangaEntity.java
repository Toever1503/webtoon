package webtoon.domains.manga.entities;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import webtoon.domains.manga.enums.EMangaDisplayType;
import webtoon.domains.manga.enums.EMangaSTS;
import webtoon.domains.manga.enums.EStatus;
import webtoon.domains.manga.enums.EMangaType;
import webtoon.domains.tag.entity.TagEntity;

@Entity
@Table(name = "tbl_manga_entity")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@Builder
@Getter
@Setter
public class MangaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private java.lang.Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "alternative_title")
    private String alternativeTitle;

    @Column(name = "excerpt")
    private String excerpt;

    @Column(name = "description")
    private String description;

    @Column(name = "manga_name")
    private String mangaName;

    @Column(name = "featured_image")
    private String featuredImage;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "manga_status")
    private EMangaSTS mangaStatus;

    @Column(name = "comment_count")
    private Integer commentCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "manga_type")
    private EMangaType mangaType;

    @Enumerated(EnumType.STRING)
    @Column(name = "display_type")
    private EMangaDisplayType displayType;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date createdAt;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date modifiedAt;

    @Column(name = "rating")
    private Float rating;

    @Column(name = "view_count")
    private Integer viewCount;

    //private  created_by;
//
//private modified_by;
    @OneToMany(mappedBy = "manga", cascade = CascadeType.ALL)
    private Set<MangaVolumeEntity> volumeEntities;

    @ManyToMany
    @JoinTable(name = "tbl_manga_genre_relation",
            joinColumns = @JoinColumn(name = "manga_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<MangaGenreEntity> genres;

    @ManyToMany
    @JoinTable(name = "tbl_manga_author_relation",
            joinColumns = @JoinColumn(name = "manga_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )

    private Set<MangaAuthorEntity> authors;

    @Transient
    private List<TagEntity> tags;
}
