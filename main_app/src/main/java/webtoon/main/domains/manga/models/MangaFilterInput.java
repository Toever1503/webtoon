package webtoon.main.domains.manga.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import webtoon.main.domains.manga.enums.EMangaSTS;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MangaFilterInput {
    private EMangaSTS status;

    private String q;


    private Integer releaseYear;
    private Boolean isShow;
    private Long genreId;
    private Long authorId;
}
