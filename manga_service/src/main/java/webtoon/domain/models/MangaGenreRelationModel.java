package webtoon.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import webtoon.domain.entities.MangaEntity;
import webtoon.domain.entities.MangaGenreEntity;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MangaGenreRelationModel {

	private Long id;
	
	private MangaGenreEntity genreId;
	
	private MangaEntity mangaId; 
}
