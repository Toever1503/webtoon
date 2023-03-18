package webtoon.domains.manga.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import webtoon.domains.manga.entities.MangaVolumeEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MangaChapterModel {
	private Long id;
	
	private MangaVolumeEntity mangaId;
	
	private String name;
	
	private String content;
	
	private Integer chapterIndex;
	
	private Boolean requiredVip;
}
