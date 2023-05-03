package webtoon.domains.manga.entities;

import javax.persistence.*;

import lombok.*;

@Entity
@Table(name ="tbl_manga_genre")
@NoArgsConstructor(access = AccessLevel.PRIVATE,force = true)
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MangaGenreEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "slug")
	private String slug;

	@Column(name = "manga_count", columnDefinition = "int default 0")
	private Integer mangaCount;


}
