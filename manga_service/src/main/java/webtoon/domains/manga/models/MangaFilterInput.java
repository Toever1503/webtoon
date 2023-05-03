package webtoon.domains.manga.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import webtoon.domains.manga.enums.EStatus;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MangaFilterInput {
    private EStatus status;

    private String q;

    private Long generId;

    private String releaseYear;
}
