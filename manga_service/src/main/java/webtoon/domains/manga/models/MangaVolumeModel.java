package webtoon.domains.manga.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import webtoon.domains.manga.entities.Long;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MangaVolumeModel {
	private java.lang.Long id;

	private Long mangaId;
	
	private String name;
	
	private Integer volumeIndex;
}
