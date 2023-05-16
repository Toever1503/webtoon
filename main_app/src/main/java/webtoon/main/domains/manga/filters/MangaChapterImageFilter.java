package webtoon.main.domains.manga.filters;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MangaChapterImageFilter {
	private String image;

	private Integer imageIndex;
}
