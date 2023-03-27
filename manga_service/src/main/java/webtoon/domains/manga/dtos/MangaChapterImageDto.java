package webtoon.domains.manga.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import webtoon.domains.manga.entities.MangaChapterEntity;
import webtoon.domains.manga.entities.MangaChapterImageEntity;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MangaChapterImageDto {
	private Long id;
	
	private MangaChapterEntity mangaChapterId;
	
	private String image;
	
	private Integer imageIndex;
	
	public static MangaChapterImageDto toDto(MangaChapterImageEntity mangaEntity) {
		if(mangaEntity == null) return null;
		
		return MangaChapterImageDto.builder()
				.id(mangaEntity.getId())
				.mangaChapterId(mangaEntity.getMangaChapter())
				.image(mangaEntity.getImage())
				.imageIndex(mangaEntity.getImageIndex())
				.build();
	}
}
