package webtoon.main.domains.manga.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import webtoon.main.account.entities.UserEntity;
import webtoon.main.domains.manga.entities.MangaChapterEntity;
import webtoon.main.domains.manga.entities.MangaEntity;

import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ReadHistoryDto {
    private Long id;

    private Long mangaEntity;

    private Long chapterEntity;

    private Long createdBy;

    private Date createdDate;

}
