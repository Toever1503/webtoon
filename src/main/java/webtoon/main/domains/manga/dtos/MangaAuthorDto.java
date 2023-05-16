package webtoon.main.domains.manga.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import webtoon.main.domains.manga.entities.MangaAuthorEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MangaAuthorDto {

	private Long id;

	private String name;
	private Long mangaCount;
	public static MangaAuthorDto toDto(MangaAuthorEntity mangaEntity) {
		if(mangaEntity == null) return null;
		
		return MangaAuthorDto.builder()
				.id(mangaEntity.getId())
				.name(mangaEntity.getName())
				.build();
	}
}
