package webtoon.domains.manga.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import webtoon.domains.manga.entities.MangaEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MangaVolumeModel {
	private Long id;

	private MangaEntity mangaId;
	
	private String name;
	
	private Integer volumeIndex;
}
