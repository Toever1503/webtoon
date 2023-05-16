package webtoon.main.domains.manga.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import webtoon.main.domains.manga.entities.MangaChapterImageEntity;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MangaChapterImageDto {
	private Long id;
	
	private Long mangaChapterId;
	
	private String image;
	
	private Integer imageIndex;
	
	public static MangaChapterImageDto toDto(MangaChapterImageEntity mangaEntity) {
		if(mangaEntity == null) return null;
		
		return MangaChapterImageDto.builder()
				.id(mangaEntity.getId())
				.mangaChapterId(mangaEntity.getMangaChapter().getId())
				.image(mangaEntity.getImage())
				.imageIndex(mangaEntity.getImageIndex())
				.build();
	}
}
