package webtoon.domains.manga.services.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import webtoon.domains.manga.dtos.MangaDto;
import webtoon.domains.manga.entities.MangaEntity;
import webtoon.domains.manga.models.MangaModel;
import webtoon.domains.manga.repositories.IMangaRepository;
import webtoon.domains.manga.services.IMangaService;
//import webtoon.utils.exception.CustomHandleException;

@Service
@Transactional
public class IMangaServiceImpl implements IMangaService {
    @Autowired
    private IMangaRepository mangaRepository;

    @Override
    public MangaDto add(MangaModel model) {
        MangaEntity mangaEntity = MangaEntity.builder()
                .title(model.getTitle())
                .alternativeTitle(model.getAlternativeTitle())
                .concerpt(model.getConcerpt())
                .description(model.getDescription())
                .mangaName(model.getMangaName())
                .featuredImage(model.getFeaturedImage())
                .status(model.getStatus()) // enum status
                .mangaSts(model.getMangaSts())
                .commentCount(model.getCommentCount())
                .mangaType(model.getMangaType())
                .rating(model.getRating())
                .created_at(model.getCreated_at())
                .modifed_at(model.getModifed_at())
                .build();
        this.mangaRepository.saveAndFlush(mangaEntity);
        return MangaDto.builder()
                .title(mangaEntity.getTitle())
                .alternativeTitle(mangaEntity.getAlternativeTitle())
                .concerpt(mangaEntity.getConcerpt())
                .description(mangaEntity.getDescription())
                .mangaName(mangaEntity.getMangaName())
                .featuredImage(mangaEntity.getFeaturedImage())
                .status(mangaEntity.getStatus())
                .mangaSts(mangaEntity.getMangaSts())
                .commentCount(mangaEntity.getCommentCount())
                .mangaType(mangaEntity.getMangaType())
                .rating(mangaEntity.getRating())
                .view_Count(mangaEntity.getView_Count())
                .created_at(mangaEntity.getCreated_at())
                .modifed_at(mangaEntity.getModifed_at())
                .build();

    }

    @Override
    public MangaDto update(MangaModel model) {

        MangaEntity mangaEntity = this.getById(model.getId());

        mangaEntity.setTitle(model.getTitle());
        mangaEntity.setAlternativeTitle(model.getAlternativeTitle());
        mangaEntity.setConcerpt(model.getConcerpt());
        mangaEntity.setDescription(model.getDescription());
        mangaEntity.setMangaName(model.getMangaName());
        mangaEntity.setFeaturedImage(model.getFeaturedImage());
        mangaEntity.setStatus(model.getStatus());
        mangaEntity.setMangaSts(model.getMangaSts());
        mangaEntity.setMangaType(model.getMangaType());
        mangaEntity.setCommentCount(model.getCommentCount());
        mangaEntity.setCreated_at(model.getCreated_at());
        mangaEntity.setModifed_at(model.getModifed_at());
        mangaEntity.setRating(model.getRating());
        mangaEntity.setView_Count(model.getView_Count());
        mangaRepository.saveAndFlush(mangaEntity);
        return MangaDto.builder()
                .title(mangaEntity.getTitle())
                .alternativeTitle(mangaEntity.getAlternativeTitle())
                .concerpt(mangaEntity.getConcerpt())
                .description(mangaEntity.getDescription())
                .mangaName(mangaEntity.getMangaName())
                .featuredImage(mangaEntity.getFeaturedImage())
                .status(mangaEntity.getStatus())
                .mangaSts(mangaEntity.getMangaSts())
                .commentCount(mangaEntity.getCommentCount())
                .mangaType(mangaEntity.getMangaType())
                .rating(mangaEntity.getRating())
                .view_Count(mangaEntity.getView_Count())
                .created_at(mangaEntity.getCreated_at())
                .modifed_at(mangaEntity.getModifed_at())
                .build();

    }


    public MangaEntity getById(Long id) {
        return this.mangaRepository.findById(id).orElseThrow(() -> new RuntimeException("22"));
    }


    @Override
    public boolean deleteById(Long id) {
        try {
            this.mangaRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
    }

    @Override
    public Page<MangaDto> filter(Pageable pageable, Specification<MangaEntity> specs) {
        return mangaRepository.findAll(specs, pageable).map(MangaDto::toDto);
    }
    
    
}
