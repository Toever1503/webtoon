package webtoon.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import webtoon.enums.EMangaSTS;
import webtoon.enums.EMangaStatus;
import webtoon.enums.EMangaType;

@Entity
@Table
@AllArgsConstructor
@Builder
@Getter
@Setter
public class MangaEntity {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "id")
private Long id;

@Column(name = "title")
private String title;

@Column(name = "alternative_title")
private String alternativeTitle;

@Column(name = "concerpt")
private String concerpt;

@Column(name = "description")
private String description;

@Column(name = "manga_name")
private String mangaName;

@Column(name = "featured_image")
private String featuredImage ;

@JoinColumn(name = "status")
private EMangaStatus status;

@JoinColumn(name = "manga_sts")
private EMangaSTS mangaSts;

@Column(name = "comment_count")
private Integer commentCount;

@JoinColumn(name = "manga_type")
private EMangaType mangaType;

@Column
@Temporal(TemporalType.TIMESTAMP)
@UpdateTimestamp
private Date created_at;

@Column
@Temporal(TemporalType.TIMESTAMP)
@UpdateTimestamp
private Date modifed_at;

@Column(name = "rating")
private Integer rating;

@Column(name = "view_count")
private Integer view_Count;

@Column
@Temporal(TemporalType.TIMESTAMP)
@UpdateTimestamp
private Date published_date;



//private  created_by;
//
//private modified_by;

}
