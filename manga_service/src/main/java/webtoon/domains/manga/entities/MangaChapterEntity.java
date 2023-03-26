package webtoon.domains.manga.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
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
	
	@JoinColumn(name = "manga_id")
	@ManyToOne
	private MangaVolumeEntity mangaVolume;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "content")
	private String content;
	
	@Column(name = "chapter_index")
	private Integer chapterIndex;
	
	@Column(name = "required_vip")
	private Boolean requiredVip;
}
