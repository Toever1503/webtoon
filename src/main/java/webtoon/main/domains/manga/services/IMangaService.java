package webtoon.main.domains.manga.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import webtoon.main.domains.manga.dtos.MangaDto;
import webtoon.main.domains.manga.entities.MangaEntity;
import webtoon.main.domains.manga.entities.MangaGenreEntity;
import webtoon.main.domains.manga.enums.EMangaDisplayType;
import webtoon.main.domains.manga.enums.EMangaSTS;
import webtoon.main.domains.manga.enums.EMangaType;
import webtoon.main.domains.manga.enums.EStatus;
import webtoon.main.domains.manga.models.MangaModel;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Set;

public interface IMangaService {

    MangaDto add(MangaModel model);

    MangaEntity getById(Long id);

    void increaseView(Long mangaId);



    MangaEntity getByIdAndCb(Long mangaId, Long createId);

    Double getRating(Long id);


    List<MangaEntity> getALLByGenres(List<Long> ids);

    MangaDto getByMangaId(Long id);

    boolean deleteById(Long id);

    MangaDto update(MangaModel model);


    void setMangaTypeAndDisplayType(Long id, EMangaType mangaType, EMangaDisplayType displayType);

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

    Page<MangaEntity> filterEntitiesByTag(Long id);
}
