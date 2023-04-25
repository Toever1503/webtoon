package webtoon.domains.manga.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import webtoon.domains.manga.dtos.MangaDto;
import webtoon.domains.manga.entities.MangaEntity;
import webtoon.domains.manga.enums.EMangaDisplayType;
import webtoon.domains.manga.enums.EMangaSTS;
import webtoon.domains.manga.enums.EMangaType;
import webtoon.domains.manga.enums.EStatus;
import webtoon.domains.manga.models.MangaModel;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface IMangaService {

    MangaDto add(MangaModel model);

    MangaEntity getById(java.lang.Long id);


    MangaEntity getByIdAndCb(Long mangaId, Long createId);

    Double getRating(Long id);

    boolean deleteById(java.lang.Long id);

    MangaDto update(MangaModel model);


    void setMangaTypeAndDisplayType(java.lang.Long id, EMangaType mangaType, EMangaDisplayType displayType);

    Page<MangaEntity> filterBy(String s, Pageable page);

    Page<MangaDto> filter(Pageable pageable, Specification<MangaEntity> specs);
    Page<MangaEntity> filterEntities(Pageable pageable, Specification<MangaEntity> specs);

    MangaDto findById(Long id);

    void resetMangaType(Long mangaId);

    void changeReleaseStatus(Long id, EMangaSTS mangaSTS);

    void changeStatus(Long id, EStatus status);


    Page<MangaDto> findAllById(Long id);

    /*
    calculating total manga each status
    ex:
    total manga: 100
        drafted: 10
        published: 90
     */
    List<Object[]> calculateTotalMangaEachStatus(String q);
}
