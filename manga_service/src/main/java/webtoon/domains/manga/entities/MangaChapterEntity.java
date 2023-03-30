package webtoon.domains.manga.entities;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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

	@OneToMany
	private List<MangaChapterImageEntity> chapterImages;
}
