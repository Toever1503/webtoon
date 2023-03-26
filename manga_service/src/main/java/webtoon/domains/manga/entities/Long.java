package webtoon.domains.manga.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import webtoon.domains.manga.enums.EMangaSTS;
import webtoon.domains.manga.enums.EMangaStatus;
import webtoon.domains.manga.enums.EMangaType;

@Entity
@Table(name = "tbl_manga_entity")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Long {
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
    private EMangaStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "manga_status")
    private EMangaSTS mangaStatus;

    @Column(name = "comment_count")
    private Integer commentCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "manga_type")
    private EMangaType mangaType;

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

}
