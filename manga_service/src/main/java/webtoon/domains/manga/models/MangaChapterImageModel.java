package webtoon.domains.manga.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import webtoon.domains.manga.entities.MangaChapterEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MangaChapterImageModel {
	private Long id;

	private MangaChapterEntity mangaChapterId;

	private String image;

	private Integer imageIndex;
}
