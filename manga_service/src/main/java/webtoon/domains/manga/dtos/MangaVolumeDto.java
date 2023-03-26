package webtoon.domains.manga.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import webtoon.domains.manga.entities.Long;
import webtoon.domains.manga.entities.MangaVolumeEntity;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MangaVolumeDto {
	private java.lang.Long id;
	
	private Long mangaId;
	
	private String name;
	
	private Integer volumeIndex;
	
	public static MangaVolumeDto toDto(MangaVolumeEntity entity) {
		if(entity == null) return null;
		
		return MangaVolumeDto.builder()
				.id(entity.getId())
				.mangaId(entity.getMangaId())
				.name(entity.getName())
				.volumeIndex(entity.getVolumeIndex())
				.build();
	}
}
