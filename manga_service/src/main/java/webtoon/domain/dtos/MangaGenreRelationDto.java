package webtoon.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import webtoon.domain.entities.MangaEntity;
import webtoon.domain.entities.MangaGenreEntity;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MangaGenreRelationDto {
	private Long id;
	
	private MangaGenreEntity genreId;
	
	private MangaEntity mangaId; 
}
