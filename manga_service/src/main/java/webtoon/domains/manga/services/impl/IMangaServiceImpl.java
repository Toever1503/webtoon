package webtoon.domains.manga.services.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import webtoon.domains.manga.dtos.MangaDto;
import webtoon.domains.manga.entities.MangaEntity;
import webtoon.domains.manga.enums.EMangaType;
import webtoon.domains.manga.mappers.MangaMapper;
import webtoon.domains.manga.models.MangaModel;
import webtoon.domains.manga.repositories.IMangaRepository;
import webtoon.domains.manga.services.IMangaService;
import webtoon.utils.ASCIIConverter;
//import webtoon.utils.exception.CustomHandleException;

@Service
@Transactional
public class IMangaServiceImpl implements IMangaService {
    @Autowired
    private IMangaRepository mangaRepository;

    @Autowired
    private MangaMapper mangaMapper;

    @Override
    public MangaDto add(MangaModel model) {
        MangaEntity mangaEntity = this.mangaMapper.toEntity(model);

        mangaEntity.setMangaName(ASCIIConverter.utf8ToAscii(model.getTitle()));
        mangaEntity.setRating(0F);
        mangaEntity.setViewCount(0);
        mangaEntity.setCommentCount(0);

        this.mangaRepository.saveAndFlush(mangaEntity);
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
    public void setMangaType(java.lang.Long id, EMangaType type) {
        MangaEntity entity = this.getById(id);
        if (!entity.getMangaType().equals(EMangaType.UNSET))
            throw new RuntimeException("Manga has already set type");
        entity.setMangaType(type);
        this.mangaRepository.saveAndFlush(entity);
    }


    public MangaEntity getById(java.lang.Long id) {
        return this.mangaRepository.findById(id).orElseThrow(() -> new RuntimeException("22"));
    }


    @Override
    public boolean deleteById(java.lang.Long id) {
        try {
            this.mangaRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
    }



    @Override
    public Page<MangaEntity> filterBy(String s, Pageable page){
        return this.mangaRepository.findAll((root, query, cb) -> {
            return cb.like(root.get("title"),"%" + s + "%");
        },page);
    }

    @Override
    public Page<MangaDto> filter(Pageable pageable, Specification<MangaEntity> specs) {
        return mangaRepository.findAll(specs, pageable).map(MangaDto::toDto);
    }

    @Override
    public MangaDto findById(Long id){
        return MangaDto.toDto(this.getById(id));
    }

}
