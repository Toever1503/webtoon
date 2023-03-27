package webtoon.domains.manga.dtos;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import webtoon.domains.manga.entities.MangaEntity;
import webtoon.domains.manga.enums.EMangaSTS;
import webtoon.domains.manga.enums.EMangaStatus;
import webtoon.domains.manga.enums.EMangaType;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MangaDto {
	private Long id;
	private String title;
	private String alternativeTitle;
	private String concerpt;
	private String description;
	 String mangaName;
	private String featuredImage ;
	private EMangaStatus status;
	private EMangaSTS mangaSts;
	private Integer commentCount;
	private EMangaType mangaType;
	private Date created_at;
	private Date modifed_at;
	private Integer rating;
	private Integer view_Count;
	private Date published_date;
	
	public static MangaDto toDto(MangaEntity mangaEntity) {
		if(mangaEntity == null) return null;
		
		return MangaDto.builder()
				.id(mangaEntity.getId())
				.title(mangaEntity.getTitle())
				.alternativeTitle(mangaEntity.getAlternativeTitle())
				.concerpt(mangaEntity.getConcerpt())
				.description(mangaEntity.getDescription())
				.mangaName(mangaEntity.getMangaName())
				.featuredImage(mangaEntity.getFeaturedImage())
//				.status(null)
//				.mangaSts(null)
				.commentCount(mangaEntity.getCommentCount())
//				.mangaType(null)
				.rating(mangaEntity.getRating())
				.view_Count(mangaEntity.getView_Count())
				.created_at(mangaEntity.getCreated_at())
				.modifed_at(mangaEntity.getModifed_at())
				.build();
	}
}
