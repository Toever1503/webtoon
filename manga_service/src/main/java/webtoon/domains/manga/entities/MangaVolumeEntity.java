package webtoon.domains.manga.entities;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Table(name = "tbl_manga_volume_entity")
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
	
	@OneToMany(mappedBy="mangaVolume")
    private Set<MangaChapterEntity> chapterEntities;
}
