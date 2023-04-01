package webtoon.domains.manga.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MangaChapterFilterInput {
    private String q;
    private Long volumeId;
    private Long mangaId;

    private Integer chapterIndex;
}
