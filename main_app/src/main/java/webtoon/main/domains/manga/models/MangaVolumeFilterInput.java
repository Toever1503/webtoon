package webtoon.main.domains.manga.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MangaVolumeFilterInput {
    private String q;
    private Long mangaId;

    private Integer volumeIndex;
}
