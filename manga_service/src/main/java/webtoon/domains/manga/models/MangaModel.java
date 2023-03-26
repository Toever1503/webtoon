package webtoon.domains.manga.models;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import webtoon.domains.manga.enums.EMangaSTS;
import webtoon.domains.manga.enums.EMangaStatus;
import webtoon.domains.manga.enums.EMangaType;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class MangaModel {
	private Long id;
	private String title;
	private String alternativeTitle;
	private String excerpt;
	private String description;
	private String mangaName;
	private String featuredImage ;
	private EMangaStatus status;
	private EMangaSTS mangaStatus;
	private EMangaType mangaType;
	private Date createdAt;
	private Date modifiedAt;
	
}
