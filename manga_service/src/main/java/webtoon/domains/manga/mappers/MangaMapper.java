package webtoon.domains.manga.mappers;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import webtoon.domains.manga.dtos.MangaDto;
import webtoon.domains.manga.entities.Long;
import webtoon.domains.manga.models.MangaModel;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MangaMapper {
    public MangaDto toDto(Long entity) {
        return MangaDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .alternativeTitle(entity.getAlternativeTitle())
                .excerpt(entity.getExcerpt())
                .description(entity.getDescription())
                .mangaName(entity.getMangaName())
                .featuredImage(entity.getFeaturedImage())
                .status(entity.getStatus())
                .mangaStatus(entity.getMangaStatus())
                .commentCount(entity.getCommentCount())
                .mangaType(entity.getMangaType())
                .rating(entity.getRating())
                .viewCount(entity.getViewCount())
                .createdAt(entity.getCreatedAt())
                .modifiedAt(entity.getModifiedAt())
                .build();
    }

    public Long toEntity(MangaModel input) {
        return Long.builder()
                .id(input.getId())
                .title(input.getTitle())
                .alternativeTitle(input.getAlternativeTitle())
                .excerpt(input.getExcerpt())
                .description(input.getDescription())
                .mangaName(input.getMangaName())
                .featuredImage(input.getFeaturedImage())
                .status(input.getStatus()) // enum status
                .mangaStatus(input.getMangaStatus())
                .mangaType(input.getMangaType())
                .createdAt(input.getCreatedAt())
                .modifiedAt(input.getModifiedAt())
                .build();
    }

    public List<MangaDto> toDtoList(List<Long> entities) {
        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }

    public Page<MangaDto> toDtoPage(Page<Long> entityPage) {
        return entityPage.map(this::toDto);
    }
}
