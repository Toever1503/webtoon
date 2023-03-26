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
public class MangaAuthorRelationEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private java.lang.Long id;
	
	@JoinColumn(name = "author_id")
	@ManyToOne
	private MangaAuthorEntity authorId;
	
	
	@JoinColumn(name = "manga_id")
	@ManyToOne
	private Long mangaId;
	
	@Column(name = "author_type")
	private String authorType;

}
