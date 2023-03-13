package webtoon.domains.manga.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MangaGenreModel {
	private Long id;
	
	private String name;
	
	private String slug;
}
