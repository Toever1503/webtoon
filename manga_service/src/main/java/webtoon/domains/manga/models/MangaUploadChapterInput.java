package webtoon.domains.manga.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MangaUploadChapterInput {
    private Long id;
    private String chapterName;
    private Integer chapterIndex;
    private Long volumeID;
    private Long mangaID;
    private Boolean isRequiredVip;

    private String chapterContent;
    private List<String> chapterImages;
}
