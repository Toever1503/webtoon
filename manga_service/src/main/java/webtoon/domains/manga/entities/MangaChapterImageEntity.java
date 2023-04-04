package webtoon.domains.manga.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.*;

@Entity
@Table(name = "tbl_manga_chapter_image_entity")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
public class MangaChapterImageEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@JoinColumn(name = "manga_chapter_id")
	@ManyToOne
	private MangaChapterEntity mangaChapter;
	
	@Column(name = "image")
	private String image;

	@Column(name = "file_id")
	private Long fileId;
	
	@Column(name = "image_index")
	private Integer imageIndex;
}
