package webtoon.main.domains.manga.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import webtoon.main.domains.manga.dtos.ReadHistoryDto;
import webtoon.main.domains.manga.dtos.ReadHistoryDto2;
import webtoon.main.domains.manga.entities.MangaEntity;
import webtoon.main.domains.manga.entities.ReadHistory;
import webtoon.main.domains.manga.models.ReadHistoryModel;

import java.util.List;
import java.util.Optional;

public interface IReadHistoryService {
    ReadHistory addReadHistory(ReadHistoryModel model);

    ReadHistoryDto update(ReadHistoryModel model);

    boolean deleteById(Long id);

    ReadHistory  getById(Long id);

   ReadHistory findByMangaId(Long id);

    ReadHistory  findByCBAndMG(Long idCreatBy, Long idManga);

    ReadHistory save(ReadHistory item);

    List<ReadHistoryDto2> findAllByCreatedBy(Long id);

    Page<ReadHistoryDto2> findAllByCreatedBy(Long userId, Pageable pageable);
}
