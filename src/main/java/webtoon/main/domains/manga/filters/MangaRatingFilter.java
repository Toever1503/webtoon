package webtoon.main.domains.manga.filters;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import webtoon.main.domains.manga.entities.MangaEntity;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MangaRatingFilter {
    private Long id;

    private Long mangaEntity;

    private String createdBy;

    private String rate;
}
