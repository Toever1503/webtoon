package webtoon.main.domains.manga.services.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import webtoon.main.account.configs.security.SecurityUtils;
import webtoon.main.domains.manga.dtos.MangaDto;
import webtoon.main.domains.manga.entities.MangaEntity;
import webtoon.main.domains.manga.entities.MangaGenreEntity;
import webtoon.main.domains.manga.enums.EMangaDisplayType;
import webtoon.main.domains.manga.enums.EMangaSTS;
import webtoon.main.domains.manga.enums.EMangaType;
import webtoon.main.domains.manga.enums.EStatus;
import webtoon.main.domains.manga.mappers.MangaMapper;
import webtoon.main.domains.manga.models.MangaModel;
import webtoon.main.domains.tag.entity.enums.ETagType;
import webtoon.main.domains.tag.service.ITagService;
import webtoon.main.domains.manga.entities.MangaEntity_;
import webtoon.main.domains.manga.repositories.*;
import webtoon.main.domains.manga.services.IMangaService;
import webtoon.main.storage.domain.dtos.FileDto;
import webtoon.main.storage.domain.services.IFileService;
import webtoon.main.utils.ASCIIConverter;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
//import webtoon.main.utils.exception.CustomHandleException;

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

    @Autowired
    private IFileService fileService;


    @Override
    public MangaDto add(MangaModel model) {
        MangaEntity mangaEntity = this.mangaMapper.toEntity(model);
        mangaEntity.setMangaName(ASCIIConverter.utf8ToAscii(model.getTitle())
                .replace(" ", "-")
                .replace("?", "-")
                .toLowerCase());

        if (model.getId() != null) {
            MangaEntity originalEntity = this.getById(model.getId());
            mangaEntity.setVolumeEntities(originalEntity.getVolumeEntities());
            mangaEntity.setChapters(originalEntity.getChapters());

            mangaEntity.setRating(originalEntity.getRating());
            mangaEntity.setViewCount(originalEntity.getViewCount());
            mangaEntity.setCommentCount(originalEntity.getCommentCount());
        } else {
            mangaEntity.setRating(0F);
            mangaEntity.setViewCount(0);
            mangaEntity.setCommentCount(0);
        }

        mangaEntity.setGenres(genreRepository.findAllById(model.getGenres()).stream().collect(Collectors.toSet()));
        mangaEntity.setAuthors(authorRepository.findAllById(model.getAuthors()).stream().collect(Collectors.toSet()));

        mangaEntity.setCreatedBy(SecurityUtils.getCurrentUser().getUser());
        mangaEntity.setModifiedBy(mangaEntity.getCreatedBy());
        this.mangaRepository.saveAndFlush(mangaEntity);
        if (model.getTags() != null)
            mangaEntity.setTags(tagService.saveTagRelation(mangaEntity.getId(), model.getTags(), ETagType.MANGA));
        else
            mangaEntity.setTags(Collections.EMPTY_LIST);

        if (model.getFeaturedImageFile() != null) {
            FileDto fileDto = fileService.uploadFile(model.getFeaturedImageFile(), "manga/" + mangaEntity.getId() +"/");
            mangaEntity.setFeaturedImage(fileDto.getUrl());
        }

        return this.mangaMapper.toDto(mangaEntity);
    }

    @Override
    public MangaDto update(MangaModel model) {

        MangaEntity mangaEntity = this.getById(model.getId());
        mangaEntity.setTitle(model.getTitle());
        mangaEntity.setExcerpt(model.getExcerpt());
        mangaEntity.setDescription(model.getDescription());
        mangaEntity.setMangaName(model.getMangaName());
        mangaEntity.setFeaturedImage(model.getFeaturedImage());
        mangaEntity.setStatus(model.getStatus());
        mangaEntity.setMangaStatus(model.getMangaStatus());
        mangaEntity.setMangaType(model.getMangaType());
        mangaEntity.setCreatedAt(model.getCreatedAt());
        mangaEntity.setModifiedAt(model.getModifiedAt());
        mangaEntity.setModifiedBy(SecurityUtils.getCurrentUser().getUser());
        mangaRepository.saveAndFlush(mangaEntity);
        return MangaDto.builder()
                .title(mangaEntity.getTitle())
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
    public void setMangaTypeAndDisplayType(Long id, EMangaType mangaType, EMangaDisplayType displayType) {
        MangaEntity entity = this.getById(id);
        entity.setMangaType(mangaType);
        entity.setDisplayType(displayType);

        if (entity.getVolumeEntities() != null) {
            entity.getVolumeEntities().forEach(vol -> {
                this.mangaChapterRepository.deleteALlByMangaVolumeId(vol.getId());
            });
        }

        this.mangaVolumeRepository.deleteAllByMangaId(id);
        this.mangaChapterRepository.deleteALlByMangaId(id);

        entity.setVolumeEntities(null);
        entity.setChapters(null);
        this.mangaRepository.saveAndFlush(entity);
    }


    public MangaEntity getById(Long id ) {
        MangaEntity entity = this.mangaRepository.findById(id).orElseThrow(() -> new RuntimeException("22"));

        entity.setTags(this.tagService.findAllByObjectIdAndType(entity.getId(), ETagType.MANGA));
        return entity;
    }

    @Override
    public void increaseView(Long mangaId) {
        MangaEntity mangaEntity = this.getById(mangaId);
        if(mangaEntity.getViewCount() == null)
            mangaEntity.setViewCount(0);
        mangaEntity.setViewCount(mangaEntity.getViewCount() + 1);
        this.mangaRepository.saveAndFlush(mangaEntity);
    }


    @Override
    public MangaEntity getByIdAndCb(Long mangaId, Long createId){
        MangaEntity entity = this.mangaRepository.getByIdAndCb(mangaId, createId);
        return entity;
    }

    @Override
    public Double getRating(Long id){
            return  this.mangaRepository.getRatingManga(id);
    }

    @Override
    public List<MangaEntity> getALLByGenres(List<Long> ids) {
        return this.mangaRepository.findByGenresIn(ids);
    }


    @Override
    public MangaDto getByMangaId(Long id){
        MangaEntity entity = this.mangaRepository.getByMangaId(id);
        return MangaDto.builder()
                .id(entity.getId())
                .mangaName(entity.getMangaName())
                .title(entity.getTitle())
                .createdBy(entity.getCreatedBy())
                .featuredImage(entity.getFeaturedImage())
                .build();
    }



    @Override
    public boolean deleteById(Long id) {
        try {
            MangaEntity entity = this.getById(id);
            entity.setDeletedAt(Calendar.getInstance().getTime());
            entity.setModifiedBy(SecurityUtils.getCurrentUser().getUser());
            this.mangaRepository.saveAndFlush(entity);
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
        Page<MangaEntity> mangaEntityPage = mangaRepository.findAll(specs, pageable);

        return mangaEntityPage.map(entity -> {
            entity.setTags(this.tagService.findAllByObjectIdAndType(entity.getId(), ETagType.MANGA));
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

    @Override
    public Page<MangaEntity> filterEntitiesByTag(Long id) {
        return null;
    }
}
