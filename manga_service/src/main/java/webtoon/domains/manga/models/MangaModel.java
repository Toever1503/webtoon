package webtoon.domains.manga.models;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import webtoon.domains.manga.enums.EMangaDisplayType;
import webtoon.domains.manga.enums.EMangaSTS;
import webtoon.domains.manga.enums.EStatus;
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
	private EStatus status;
	private EMangaSTS mangaStatus;
	private EMangaType mangaType;
	private Integer releaseYear;
	private EMangaDisplayType displayType;
	private Date createdAt;
	private Date modifiedAt;
	private Boolean isFree;
	private List<Long> authors;
	private List<Long> genres;
	private List<Long> tags;

}
