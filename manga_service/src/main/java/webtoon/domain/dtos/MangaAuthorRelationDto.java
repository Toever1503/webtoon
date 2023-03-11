package webtoon.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import webtoon.domain.entities.MangaAuthorEntity;
import webtoon.domain.entities.MangaEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MangaAuthorRelationDto {
	private Long id;
	
	private MangaAuthorEntity authorId;
	
	private MangaEntity mangaId;
	
	private String authorType;
}
