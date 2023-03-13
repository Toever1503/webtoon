package webtoon.domains.manga.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.IdClass;
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
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MangaGenreRelationEntity {
	@Id
	@Column(name = "id")
	@GeneratedValue()
	private Long id;
	
	
	@JoinColumn(name = "genre_id")
	@ManyToOne
	private MangaGenreEntity genreId;
	
	@ManyToOne
	@JoinColumn(name = "manga_id")
	private MangaEntity mangaId; 
}
