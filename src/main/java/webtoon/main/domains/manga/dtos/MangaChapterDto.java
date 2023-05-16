package webtoon.main.domains.manga.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import webtoon.main.domains.manga.dtos.MangaChapterImageDto;
import webtoon.main.domains.manga.entities.MangaChapterEntity;

import java.util.Collections;
import java.util.Date;
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

    private String chapterName;

    private String content;

    private Integer chapterIndex;

    private Boolean requiredVip;

    private List<MangaChapterImageDto> chapterImages;

    private Date createdAt;

    public static MangaChapterDto toDto(MangaChapterEntity mangaEntity) {
        if (mangaEntity == null) return null;

        MangaChapterDto dto = MangaChapterDto.builder()
                .id(mangaEntity.getId())
                .chapterName(mangaEntity.getChapterName())
                .content(mangaEntity.getContent())
                .chapterIndex(mangaEntity.getChapterIndex())
                .requiredVip(mangaEntity.getRequiredVip())
                .chapterImages(mangaEntity.getChapterImages() != null ? mangaEntity.getChapterImages().stream().map(MangaChapterImageDto::toDto).collect(Collectors.toList()) : Collections.EMPTY_LIST)
                .createdAt(mangaEntity.getCreatedAt())
                .build();

        if (mangaEntity.getMangaVolume() != null)
            dto.setVolumeId(mangaEntity.getMangaVolume().getId());
        return dto;
    }
}
