package webtoon.domains.manga.services;

import webtoon.domains.manga.dtos.ReadHistoryDto;
import webtoon.domains.manga.entities.ReadHistory;
import webtoon.domains.manga.models.ReadHistoryModel;

import java.util.List;
import java.util.Optional;

public interface IReadHistoryService {
    ReadHistory addReadHistory(ReadHistoryModel model);

    ReadHistoryDto update(ReadHistoryModel model);

    boolean deleteById(Long id);

    ReadHistory  getById(Long id);

   ReadHistory findByMangaId(Long id);

    ReadHistory  findByCBAndMG( Long idManga,Long idCreatBy);

    ReadHistory save(ReadHistory item);


    List<ReadHistory> getByCreatBy(Long id);
}
