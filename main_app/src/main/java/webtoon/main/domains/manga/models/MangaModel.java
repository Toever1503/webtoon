package webtoon.main.domains.manga.models;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import webtoon.main.account.entities.UserEntity;
import webtoon.main.domains.manga.enums.EMangaDisplayType;
import webtoon.main.domains.manga.enums.EMangaSTS;
import webtoon.main.domains.manga.enums.EStatus;
import webtoon.main.domains.manga.enums.EMangaType;

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

	private String featuredImage;
	private MultipartFile featuredImageFile;

	private EStatus status;
	private EMangaSTS mangaStatus;
	private EMangaType mangaType;
	private Integer releaseYear;
	private EMangaDisplayType displayType;
	private Date createdAt;
	private Date modifiedAt;
	private UserEntity createdBy;
	private Boolean isFree;
	private Boolean isShow;
	private List<Long> authors;
	private List<Long> genres;
	private List<Long> tags;

}

