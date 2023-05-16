package webtoon.main.domains.manga.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import webtoon.main.domains.manga.entities.MangaVolumeEntity;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MangaVolumeDto {
	private Long id;
	
	private Long mangaId;
	
	private String name;
	
	private Integer volumeIndex;


	public static MangaVolumeDto toDto(MangaVolumeEntity entity) {
		if(entity == null) return null;
		
		return MangaVolumeDto.builder()
				.id(entity.getId())
				.mangaId(entity.getManga().getId())
				.name(entity.getName())
				.volumeIndex(entity.getVolumeIndex())
				.build();
	}
}
