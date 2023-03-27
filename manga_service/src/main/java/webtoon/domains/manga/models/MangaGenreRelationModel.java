package webtoon.domains.manga.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import webtoon.domains.manga.entities.MangaGenreEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MangaGenreRelationModel {

	private java.lang.Long id;
	
	private MangaGenreEntity genreId;
	
	private Long mangaId;
}
