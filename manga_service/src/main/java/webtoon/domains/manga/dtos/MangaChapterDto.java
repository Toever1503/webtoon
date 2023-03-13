package webtoon.domains.manga.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import webtoon.domains.manga.entities.MangaVolumeEntity;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MangaChapterDto {
	private Long id;
	
	private MangaVolumeEntity mangaId;
	
	private String name;
	
	private String content;
	
	private Integer chapterIndex;
	
	private Boolean requiredVip;
}
