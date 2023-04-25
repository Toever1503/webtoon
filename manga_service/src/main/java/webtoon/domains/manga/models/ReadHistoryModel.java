package webtoon.domains.manga.models;

import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import webtoon.account.entities.UserEntity;
import webtoon.domains.manga.entities.MangaChapterEntity;
import webtoon.domains.manga.entities.MangaEntity;

import javax.persistence.*;
import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Getter
@Setter
@ToString
public class ReadHistoryModel {

    private Long id;

    private Long mangaEntity;

    private Long chapterEntity;

    private Long createdBy;

    private Date createdDate;


}
