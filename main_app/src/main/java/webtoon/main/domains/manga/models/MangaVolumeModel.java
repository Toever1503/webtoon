package webtoon.main.domains.manga.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MangaVolumeModel {
	private Long id;

	private Long mangaId;
	
	private String name;
	
	private Integer volumeIndex;
}
