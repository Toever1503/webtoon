package webtoon.main.domains.manga.filters;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import webtoon.main.domains.manga.entities.MangaChapterEntity;
import webtoon.main.domains.manga.entities.MangaEntity;

import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ReadHistoryFilter {

    private Long id;

    private Long mangaEntity;

    private Long chapterEntity;

    private String createdBy;

    private Date createdDate;

}
