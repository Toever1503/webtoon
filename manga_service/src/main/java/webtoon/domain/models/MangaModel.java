package webtoon.domain.models;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import webtoon.domain.enums.EMangaSTS;
import webtoon.domain.enums.EMangaStatus;
import webtoon.domain.enums.EMangaType;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class MangaModel {
	private Long id;
	private String title;
	private String alternativeTitle;
	private String concerpt;
	private String description;
	private String mangaName;
	private String featuredImage ;
	private EMangaStatus status;
	private EMangaSTS mangaSts;
	private Integer commentCount;
	private EMangaType mangaType;
	private Date created_at;
	private Date modifed_at;
	private Integer rating;
	private Integer view_Count;
	
}
