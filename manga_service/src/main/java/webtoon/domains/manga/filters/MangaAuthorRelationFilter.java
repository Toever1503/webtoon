package webtoon.domains.manga.filters;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MangaAuthorRelationFilter {
	private String authorType;
}
