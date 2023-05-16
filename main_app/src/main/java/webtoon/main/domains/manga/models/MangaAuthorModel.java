package webtoon.main.domains.manga.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MangaAuthorModel {
	
	private Long id;
	
	private String name;
	private String slug;
}
