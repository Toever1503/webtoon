package webtoon.domains.manga.dtos;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import webtoon.domains.manga.entities.MangaEntity;
import webtoon.domains.manga.enums.EMangaSTS;
import webtoon.domains.manga.enums.EStatus;
import webtoon.domains.manga.enums.EMangaType;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MangaDto {
	private java.lang.Long id;
	private String title;
	private String alternativeTitle;
	private String excerpt;
	private String description;
	 String mangaName;
	private String featuredImage ;
	private EStatus status;
	private EMangaSTS mangaStatus;
	private Integer commentCount;
	private EMangaType mangaType;
	private Date createdAt;
	private Date modifiedAt;
	private Float rating;
	private Integer viewCount;
	private Set<MangaVolumeDto> mangaVolumeDtos;

	public static MangaDto toDto(MangaEntity mangaEntity) {
		if(mangaEntity == null) return null;
		
		return MangaDto.builder()
				.id(mangaEntity.getId())
				.title(mangaEntity.getTitle())
				.alternativeTitle(mangaEntity.getAlternativeTitle())
				.excerpt(mangaEntity.getExcerpt())
				.description(mangaEntity.getDescription())
				.mangaName(mangaEntity.getMangaName())
				.featuredImage(mangaEntity.getFeaturedImage())
				.status(mangaEntity.getStatus())
				.mangaStatus(mangaEntity.getMangaStatus())
				.commentCount(mangaEntity.getCommentCount())
				.mangaType(mangaEntity.getMangaType())
				.rating(mangaEntity.getRating())
				.viewCount(mangaEntity.getViewCount())
				.createdAt(mangaEntity.getCreatedAt())
				.modifiedAt(mangaEntity.getModifiedAt())
				.build();
	}
}
