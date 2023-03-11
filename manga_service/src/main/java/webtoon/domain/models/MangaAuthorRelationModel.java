package webtoon.domain.models;

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
public class MangaAuthorRelationModel {
	private Long id;
	
	private MangaAuthorEntity authorId;
	
	private MangaEntity mangaId;
	
	private String authorType;
}
