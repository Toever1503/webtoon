package webtoon.main.domains.manga.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import webtoon.main.domains.manga.entities.ReadHistory;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ReadHistoryDto2 {
    private Long id;

    private Long mangaId;

    private Long chapterId;

    private Long createdBy;

    private String title;
    private String featuredImage;
    private Integer chapterIndex;

    private Date createdDate;
    private Boolean isFree;

    public static ReadHistoryDto2  toDto(ReadHistory entity, String title, String featuredImage, Integer chapterIndex, Boolean isFree){
        return ReadHistoryDto2.builder()
                .id(entity.getId())
                .mangaId(entity.getMangaEntity())
                .chapterId(entity.getChapterEntity())
                .createdBy(entity.getCreatedBy())
                .title(title)
                .featuredImage(featuredImage)
                .chapterIndex(chapterIndex)
                .createdDate(entity.getCreatedDate())
                .isFree(isFree)
                .build();
    }

}
