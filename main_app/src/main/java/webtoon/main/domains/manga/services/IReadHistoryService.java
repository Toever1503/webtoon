package webtoon.main.domains.manga.services;

import webtoon.main.domains.manga.dtos.ReadHistoryDto;
import webtoon.main.domains.manga.entities.ReadHistory;
import webtoon.main.domains.manga.models.ReadHistoryModel;

import java.util.Optional;

public interface IReadHistoryService {
    ReadHistory addReadHistory(ReadHistoryModel model);

    ReadHistoryDto update(ReadHistoryModel model);

    boolean deleteById(Long id);

    ReadHistory  getById(Long id);

   ReadHistory findByMangaId(Long id);

    ReadHistory  findByCBAndMG(Long idCreatBy, Long idManga);

    ReadHistory save(ReadHistory item);

}
