package webtoon.domains.manga.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import webtoon.domains.manga.enums.EMangaDisplayType;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MangaChapterFilterInput {
    private String q;
    private Long volumeId;
    private Long mangaId;

    private EMangaDisplayType displayType;
    private Integer chapterIndex;
}
