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
    private Long volumeId;
    private Long mangaID;
    private Boolean isRequiredVip;

    private String content;
    private List<String> chapterImages;
    private List<Long> oldImages;
}
