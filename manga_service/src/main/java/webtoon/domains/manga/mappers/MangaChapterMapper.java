package webtoon.domains.manga.mappers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import webtoon.domains.manga.dtos.MangaChapterDto;
import webtoon.domains.manga.entities.MangaChapterEntity;
import webtoon.domains.manga.repositories.IMangaChapterRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MangaChapterMapper {
    private final IMangaChapterRepository chapterRepository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public MangaChapterMapper(IMangaChapterRepository chapterRepository) {
        this.chapterRepository = chapterRepository;
    }

    public MangaChapterDto toDto(MangaChapterEntity entity){
        this.logger.info("MangaChapterMapper`s converting entity id: {} to dto.", entity.getId());
        return MangaChapterDto.builder()
                .id(entity.getId())
                .volumeId(entity.getMangaVolume().getId())
                .chapterName(entity.getChapterName())
                .content(entity.getContent())
                .chapterIndex(entity.getChapterIndex())
                .isRequiredVip(entity.getRequiredVip())
                .build();
    }


    public List<MangaChapterDto> toDtoList(List<MangaChapterEntity> entities){
        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }

    public Page<MangaChapterDto> toDtoPage(Page<MangaChapterEntity> entityPage){
        return entityPage.map(this::toDto);
    }
}
