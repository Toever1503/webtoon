package webtoon.main.domains.manga.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import webtoon.main.domains.manga.enums.EMangaSTS;
import webtoon.main.domains.manga.enums.EMangaType;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MangaFilterInput {
    private EMangaSTS status;
    private String q;

    private Integer releaseYear;
    private Boolean isShow;
    private Boolean isFree;
    private Long genreId;
    private Long authorId;
    private EMangaType mangaType;
}
