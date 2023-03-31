package webtoon.domains.manga.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import webtoon.domains.manga.entities.MangaChapterEntity;
import webtoon.domains.manga.entities.MangaVolumeEntity;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MangaChapterDto {
    private Long id;

    private Long volumeId;

    private String name;

    private String content;

    private Integer chapterIndex;

    private Boolean isRequiredVip;

    private List<MangaChapterImageDto> chapterImages;

    public static MangaChapterDto toDto(MangaChapterEntity mangaEntity) {
        if (mangaEntity == null) return null;

        return MangaChapterDto.builder()
                .id(mangaEntity.getId())
                .volumeId(mangaEntity.getMangaVolume().getId())
                .name(mangaEntity.getName())
                .content(mangaEntity.getContent())
                .chapterIndex(mangaEntity.getChapterIndex())
                .isRequiredVip(mangaEntity.getRequiredVip())
                .chapterImages(mangaEntity.getChapterImages() != null ? mangaEntity.getChapterImages().stream().map(MangaChapterImageDto::toDto).collect(Collectors.toList()) : Collections.EMPTY_LIST)
                .build();
    }
}
