package webtoon.domains.manga.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import webtoon.domains.manga.entities.MangaEntity;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MangaVolumeDto {
	private Long id;
	
	private MangaEntity mangaId;
	
	private String name;
	
	private Integer volumeIndex;
}
