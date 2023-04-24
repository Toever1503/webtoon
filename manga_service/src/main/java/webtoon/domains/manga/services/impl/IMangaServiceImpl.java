package webtoon.domains.manga.services.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import webtoon.domains.manga.dtos.MangaDto;
import webtoon.domains.manga.entities.MangaEntity;
import webtoon.domains.manga.enums.EMangaDisplayType;
import webtoon.domains.manga.enums.EMangaSTS;
import webtoon.domains.manga.enums.EMangaType;
import webtoon.domains.manga.enums.EStatus;
import webtoon.domains.manga.mappers.MangaMapper;
import webtoon.domains.manga.models.MangaModel;
import webtoon.domains.tag.entity.enums.ETagType;
import webtoon.domains.tag.service.ITagService;
import webtoon.domains.manga.entities.MangaEntity_;
import webtoon.domains.manga.repositories.*;
import webtoon.domains.manga.services.IMangaService;
import webtoon.utils.ASCIIConverter;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
//import webtoon.utils.exception.CustomHandleException;

@Service
@Transactional
public class IMangaServiceImpl implements IMangaService {
    @Autowired
    private IMangaRepository mangaRepository;

    @Autowired
    private MangaMapper mangaMapper;

    @Autowired
    private IMangaVolumeRepository mangaVolumeRepository;

    @Autowired
    private IMangaChapterImageRepository mangaChapterImageRepository;

    @Autowired
    private IMangaChapterRepository mangaChapterRepository;
    @Autowired
    private IMangaGenreRepository genreRepository;

    @Autowired
    private IMangaAuthorRepository authorRepository;
    @Autowired
    private ITagService tagService;


    @Override
    public MangaDto add(MangaModel model) {
        MangaEntity mangaEntity = this.mangaMapper.toEntity(model);
        mangaEntity.setMangaName(ASCIIConverter.utf8ToAscii(model.getTitle()));


        if (model.getId() != null) {
            MangaEntity originalEntity = this.getById(model.getId());
            mangaEntity.setVolumeEntities(originalEntity.getVolumeEntities());
            mangaEntity.setChapters(originalEntity.getChapters());
        } else {
            mangaEntity.setRating(0F);
            mangaEntity.setViewCount(0);
            mangaEntity.setCommentCount(0);
        }

        mangaEntity.setGenres(genreRepository.findAllById(model.getGenres()).stream().collect(Collectors.toSet()));
        mangaEntity.setAuthors(authorRepository.findAllById(model.getAuthors()).stream().collect(Collectors.toSet()));

        this.mangaRepository.saveAndFlush(mangaEntity);
        if (model.getTags() != null)
            mangaEntity.setTags(tagService.saveTagRelation(mangaEntity.getId(), model.getTags(), ETagType.POST));
        else
            mangaEntity.setTags(Collections.EMPTY_LIST);
        return this.mangaMapper.toDto(mangaEntity);
    }

    @Override
    public MangaDto update(MangaModel model) {

        MangaEntity mangaEntity = this.getById(model.getId());
        mangaEntity.setTitle(model.getTitle());
        mangaEntity.setAlternativeTitle(model.getAlternativeTitle());
        mangaEntity.setExcerpt(model.getExcerpt());
        mangaEntity.setDescription(model.getDescription());
        mangaEntity.setMangaName(model.getMangaName());
        mangaEntity.setFeaturedImage(model.getFeaturedImage());
        mangaEntity.setStatus(model.getStatus());
        mangaEntity.setMangaStatus(model.getMangaStatus());
        mangaEntity.setMangaType(model.getMangaType());
        mangaEntity.setCreatedAt(model.getCreatedAt());
        mangaEntity.setModifiedAt(model.getModifiedAt());
        mangaRepository.saveAndFlush(mangaEntity);
        return MangaDto.builder()
                .title(mangaEntity.getTitle())
                .alternativeTitle(mangaEntity.getAlternativeTitle())
                .excerpt(mangaEntity.getExcerpt())
                .description(mangaEntity.getDescription())
                .mangaName(mangaEntity.getMangaName())
                .featuredImage(mangaEntity.getFeaturedImage())
                .status(mangaEntity.getStatus())
                .mangaStatus(mangaEntity.getMangaStatus())
                .commentCount(mangaEntity.getCommentCount())
                .mangaType(mangaEntity.getMangaType())
                .viewCount(mangaEntity.getViewCount())
                .createdAt(mangaEntity.getCreatedAt())
                .modifiedAt(mangaEntity.getModifiedAt())
                .build();

    }

    @Override
    public void setMangaTypeAndDisplayType(java.lang.Long id, EMangaType mangaType, EMangaDisplayType displayType) {
        MangaEntity entity = this.getById(id);
        entity.setMangaType(mangaType);
        entity.setDisplayType(displayType);
        this.mangaRepository.saveAndFlush(entity);
    }


    public MangaEntity getById(java.lang.Long id ) {
        MangaEntity entity = this.mangaRepository.findById(id).orElseThrow(() -> new RuntimeException("22"));

        entity.setTags(this.tagService.findAllByObjectIdAndType(entity.getId(), ETagType.POST));
        return entity;
    }

    @Override
    public MangaEntity getByIdAndCb(Long mangaId, Long createId){
        MangaEntity entity = this.mangaRepository.getByIdAndCb(mangaId, createId);
        return entity;
    }


    @Override
    public boolean deleteById(java.lang.Long id) {
        try {
            this.mangaRepository.deleteById(id);
            /*
             task:
               1.need remove image on storage service
               2. need remove chapter and volume
               3. need remove tag relation
             */

            return true;
        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
    }


    @Override
    public Page<MangaEntity> filterBy(String s, Pageable page) {
        return this.mangaRepository.findAll((root, query, cb) -> {
            return cb.like(root.get(MangaEntity_.TITLE), "%" + s + "%");
        }, page);
    }

    @Override
    public Page<MangaDto> filter(Pageable pageable, Specification<MangaEntity> specs) {
        return mangaRepository.findAll(specs, pageable).map(entity -> {
            entity.setTags(this.tagService.findAllByObjectIdAndType(entity.getId(), ETagType.POST));
            return MangaDto.toDto(entity);
        });
    }

    @Override
    public Page<MangaEntity> filterEntities(Pageable pageable, Specification<MangaEntity> specs) {
        return this.mangaRepository.findAll(specs, pageable);
    }

    @Override
    public MangaDto findById(Long id) {
        MangaEntity entity = this.getById(id);
        return MangaDto.toDto(entity);
    }

    @Override
    public void resetMangaType(Long mangaId) {
        MangaEntity entity = this.getById(mangaId);
        if (entity.getDisplayType() != null)
            if (entity.getDisplayType().equals(EMangaDisplayType.CHAP)) {
                this.mangaChapterRepository.deleteALlByMangaId(entity.getId());
            } else {
                this.mangaVolumeRepository.deleteAllByMangaId(entity.getId());
            }
        entity.setDisplayType(null);
        entity.setMangaType(EMangaType.UNSET);
        this.mangaRepository.saveAndFlush(entity);


        // task: call storage service to remove manga's folder
    }

    @Override
    public void changeReleaseStatus(Long id, EMangaSTS mangaSTS) {
        MangaEntity entity = this.getById(id);
        entity.setMangaStatus(mangaSTS);
        this.mangaRepository.saveAndFlush(entity);
    }

    @Override
    public void changeStatus(Long id, EStatus status) {
        MangaEntity entity = this.getById(id);
        entity.setStatus(status);
        this.mangaRepository.saveAndFlush(entity);
    }



    @Override
    public Page<MangaDto> findAllById(Long id) {
        Pageable pageable = PageRequest.of(0, 2).withSort(Sort.Direction.DESC, "id");
        return mangaRepository.findAllById(id, pageable);
    }

    @Override
    public List<Object[]> calculateTotalMangaEachStatus(String q) {
        return this.mangaRepository.calculateTotalMangaEachStatus(q);
    }
}
