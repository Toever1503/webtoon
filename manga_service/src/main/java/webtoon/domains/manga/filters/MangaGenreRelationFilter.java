package webtoon.domains.manga.filters;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import webtoon.domains.manga.entities.MangaGenreEntity;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MangaGenreRelationFilter {
private MangaGenreEntity genreId;
	
	private Long mangaId;
}
