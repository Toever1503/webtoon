package webtoon.domains.manga.services;

import webtoon.domains.manga.dtos.ReadHistoryDto;
import webtoon.domains.manga.entities.ReadHistory;
import webtoon.domains.manga.models.ReadHistoryModel;

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
