package webtoon.main.domains.manga.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import webtoon.main.account.entities.UserEntity;
import webtoon.main.domains.manga.entities.MangaEntity;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MangaRatingDto {
    private Long id;

    private Long mangaEntity;

    private Long createdBy;

    private Float rate;
}
