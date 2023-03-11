package webtoon.domain.entities;

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
import webtoon.domain.enums.EMangaSTS;
import webtoon.domain.enums.EMangaStatus;
import webtoon.domain.enums.EMangaType;

@Entity
@Table(name = "tbl_manga_entity")
@AllArgsConstructor
@NoArgsConstructor
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
	private String featuredImage;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private EMangaStatus status;

	@Enumerated(EnumType.STRING)
	@Column(name = "manga_sts")
	private EMangaSTS mangaSts;

	@Column(name = "comment_count")
	private Integer commentCount;

	@Enumerated(EnumType.STRING)
	@Column(name = "manga_type")
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
