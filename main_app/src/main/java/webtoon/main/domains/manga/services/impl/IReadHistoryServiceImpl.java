package webtoon.main.domains.manga.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import webtoon.main.domains.manga.dtos.ReadHistoryDto;
import webtoon.main.domains.manga.entities.ReadHistory;
import webtoon.main.domains.manga.models.ReadHistoryModel;
import webtoon.main.domains.manga.repositories.IReadHistoryRepository;
import webtoon.main.domains.manga.services.IReadHistoryService;

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
        ReadHistory entity = this.getById(model.getId());
        entity.setChapterEntity(model.getChapterEntity());
        entity.setMangaEntity(model.getMangaEntity());
        entity.setCreatedDate(model.getCreatedDate());
        this.historyRepository.saveAndFlush(entity);
        return ReadHistoryDto.builder()
                .chapterEntity(entity.getChapterEntity())
                .mangaEntity(entity.getMangaEntity())
                .createdDate(entity.getCreatedDate())
                .build();
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
    public ReadHistory findByCBAndMG(Long idCreatBy, Long idManga) {
        return this.historyRepository.findAllByCBAndMG(idCreatBy,idManga);
    }

    @Override
    public ReadHistory save(ReadHistory item) {
        return historyRepository.save(item);
    }

}
