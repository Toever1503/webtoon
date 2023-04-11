package webtoon.domains.manga.filters;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MangaChapterFilter {
private String name;
	
	private String content;
	
	private Integer chapterIndex;
	
	private Boolean requiredVip;
}
