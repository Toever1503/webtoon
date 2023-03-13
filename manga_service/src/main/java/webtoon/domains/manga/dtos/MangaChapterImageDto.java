package webtoon.domains.manga.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import webtoon.domains.manga.entities.MangaChapterEntity;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MangaChapterImageDto {
	private Long id;
	
	private MangaChapterEntity mangaChapterId;
	
	private String image;
	
	private Integer imageIndex;
}
