package webtoon.main.domains.manga.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import webtoon.main.domains.manga.entities.MangaGenreEntity;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MangaGenreDto {
	private Long id;
	
	private String name;
	
	private String slug;

	private Long mangaCount;
	
	public static MangaGenreDto toDto(MangaGenreEntity entity) {
		if(entity == null) return null;
		
		return MangaGenreDto.builder()
				.id(entity.getId())
				.name(entity.getName())
				.slug(entity.getSlug())
				.build();
	} 
}
