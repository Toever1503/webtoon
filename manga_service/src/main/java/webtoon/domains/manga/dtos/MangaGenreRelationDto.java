package webtoon.domains.manga.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import webtoon.domains.manga.entities.Long;
import webtoon.domains.manga.entities.MangaGenreEntity;
import webtoon.domains.manga.entities.MangaGenreRelationEntity;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MangaGenreRelationDto {
	private java.lang.Long id;
	
	private MangaGenreEntity genreId;
	
	private Long mangaId;
	
	public static MangaGenreRelationDto toDto(MangaGenreRelationEntity entity) {
		if(entity == null) return null;
		return MangaGenreRelationDto.builder()
				.id(entity.getId())
				.genreId(entity.getGenreId())
				.mangaId(entity.getMangaId()).build();
	}
}
