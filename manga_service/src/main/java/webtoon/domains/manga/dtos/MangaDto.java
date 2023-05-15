package webtoon.domains.manga.dtos;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import webtoon.account.entities.UserEntity;
import webtoon.domains.manga.entities.MangaEntity;
import webtoon.domains.manga.enums.EMangaDisplayType;
import webtoon.domains.manga.enums.EMangaSTS;
import webtoon.domains.manga.enums.EMangaType;
import webtoon.domains.manga.enums.EStatus;
import webtoon.domains.tag.entity.TagEntity;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MangaDto {
    private java.lang.Long id;
    private String title;
    private String excerpt;
    private String description;
    String mangaName;
    private String featuredImage;
    private EStatus status;
    private EMangaSTS mangaStatus;
    private Integer commentCount;
    private Integer releaseYear;
    private EMangaType mangaType;
    private EMangaDisplayType displayType;
    private Date createdAt;
    private Date modifiedAt;
    private Float rating;
    private Integer viewCount;
    private Boolean isFree;
    private Boolean isShow;
    private UserEntity createdBy;

    private List<MangaAuthorDto> authors;
    private List<MangaGenreDto> genres;
    private List<TagEntity> tags;

    public static MangaDto toDto(MangaEntity mangaEntity) {
        if (mangaEntity == null) return null;

        return MangaDto.builder()
                .id(mangaEntity.getId())
                .title(mangaEntity.getTitle())
                .excerpt(mangaEntity.getExcerpt())
                .description(mangaEntity.getDescription())
                .mangaName(mangaEntity.getMangaName())
                .featuredImage(mangaEntity.getFeaturedImage())
                .status(mangaEntity.getStatus())
                .mangaStatus(mangaEntity.getMangaStatus())
                .commentCount(mangaEntity.getCommentCount())
                .releaseYear(mangaEntity.getReleaseYear())
                .mangaType(mangaEntity.getMangaType())
                .displayType(mangaEntity.getDisplayType())
                .rating(mangaEntity.getRating())
                .viewCount(mangaEntity.getViewCount())
                .createdAt(mangaEntity.getCreatedAt())
                .modifiedAt(mangaEntity.getModifiedAt())
                .genres(mangaEntity.getGenres() != null ? mangaEntity.getGenres().stream().map(MangaGenreDto::toDto).collect(Collectors.toList()) : Collections.EMPTY_LIST)
                .authors(mangaEntity.getAuthors() != null ? mangaEntity.getAuthors().stream().map(MangaAuthorDto::toDto).collect(Collectors.toList()) : Collections.EMPTY_LIST)
                .tags(mangaEntity.getTags())
                .createdBy(mangaEntity.getCreatedBy())
                .isFree(mangaEntity.getIsFree())
                .isShow(mangaEntity.getIsShow())
                .build();
    }
}
