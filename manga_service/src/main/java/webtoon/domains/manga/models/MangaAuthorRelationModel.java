package webtoon.domains.manga.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import webtoon.domains.manga.entities.MangaAuthorEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MangaAuthorRelationModel {
	private java.lang.Long id;
	
	private MangaAuthorEntity authorId;
	
	private Long mangaId;
	
	private String authorType;
}
