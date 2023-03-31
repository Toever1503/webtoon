package webtoon.domains.manga.entities;

import java.util.Set;

import javax.persistence.*;

import lombok.*;

import java.util.List;

@Entity
@Table(name = "tbl_manga_chapter_entity")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
public class MangaChapterEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@ManyToOne
	private MangaVolumeEntity mangaVolume;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "content", columnDefinition = "TEXT")
	private String content;
	
	@Column(name = "chapter_index")
	private Integer chapterIndex;
	
	@Column(name = "required_vip")
	private Boolean requiredVip;

	@OneToMany(mappedBy = "mangaChapter")
	private List<MangaChapterImageEntity> chapterImages;
}
