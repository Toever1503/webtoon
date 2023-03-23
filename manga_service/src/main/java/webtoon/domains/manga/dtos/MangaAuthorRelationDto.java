package webtoon.domains.manga.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import webtoon.domains.manga.entities.MangaAuthorEntity;
import webtoon.domains.manga.entities.MangaAuthorRelationEntity;
import webtoon.domains.manga.entities.MangaEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MangaAuthorRelationDto {
	private Long id;
	
	private MangaAuthorEntity authorId;
	
	private MangaEntity mangaId;
	
	private String authorType;
	public static MangaAuthorRelationDto toDto(MangaAuthorRelationEntity mangaEntity) {
		if(mangaEntity == null) return null;
		
		return MangaAuthorRelationDto.builder()
				.id(mangaEntity.getId())
				.authorId(mangaEntity.getAuthorId())
				.mangaId(mangaEntity.getMangaId())
				.authorType(mangaEntity.getAuthorType())
				.build();
	}
}
