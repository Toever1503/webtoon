package webtoon.domains.manga.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import webtoon.domains.manga.dtos.ReadHistoryDto;
import webtoon.domains.manga.entities.ReadHistory;
import webtoon.domains.manga.models.ReadHistoryModel;
import webtoon.domains.manga.repositories.IReadHistoryRepository;
import webtoon.domains.manga.services.IReadHistoryService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class IReadHistoryServiceImpl implements IReadHistoryService {

    private final IReadHistoryRepository  historyRepository;
    public IReadHistoryServiceImpl(IReadHistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    @Override
    public ReadHistory addReadHistory(ReadHistoryModel model){
        ReadHistory entity = ReadHistory.builder()
                .chapterEntity(model.getChapterEntity())
                .mangaEntity(model.getMangaEntity())
                .createdDate(model.getCreatedDate())
                .build();
        this.historyRepository.saveAndFlush(entity);
        return entity;
    }

    @Override
    public ReadHistoryDto update(ReadHistoryModel model){
        ReadHistory entity = this.findByMangaId(model.getMangaEntity());
        if (entity != null){
            entity.setChapterEntity(model.getChapterEntity());
            entity.setMangaEntity(model.getMangaEntity());
            entity.setCreatedDate(model.getCreatedDate());
            this.historyRepository.saveAndFlush(entity);
            return ReadHistoryDto.builder()
                    .chapterEntity(entity.getChapterEntity())
                    .mangaEntity(entity.getMangaEntity())
                    .createdDate(entity.getCreatedDate())
                    .build();
        }else {
            ReadHistory entity1 = ReadHistory.builder()
                    .chapterEntity(model.getChapterEntity())
                    .mangaEntity(model.getMangaEntity())
                    .createdDate(model.getCreatedDate())
                    .build();
            this.historyRepository.saveAndFlush(entity1);
            return ReadHistoryDto.builder()
                    .chapterEntity(entity1.getChapterEntity())
                    .mangaEntity(entity1.getMangaEntity())
                    .createdDate(entity1.getCreatedDate())
                    .build();
        }

    }

    @Override
    public boolean deleteById(Long id){
        try {
            historyRepository.deleteById(id);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public ReadHistory  getById(Long id){
        ReadHistory entity = this.historyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("22"));
        return entity;
    }

    @Override
    public ReadHistory findByMangaId(Long id){
        return this.historyRepository.findAllByManga(id);
    }

    @Override
    public ReadHistory findByCBAndMG( Long idManga ,Long idCreatBy) {
        return this.historyRepository.findAllByCBAndMG(idManga,idCreatBy);
    }

    @Override
    public ReadHistory save(ReadHistory item) {
        return historyRepository.save(item);
    }


    @Override
    public List<ReadHistory> getByCreatBy(Long id){
        return historyRepository.findByCreatedBy(id).stream().map(history -> {
            return history.builder()
                    .mangaEntity(history.getMangaEntity())
                    .createdBy(history.getCreatedBy())
                    .id(history.getId())
                    .chapterEntity(history.getChapterEntity())
                    .createdDate(history.getCreatedDate())
                    .build();
        }).collect(Collectors.toList());
    }

}
