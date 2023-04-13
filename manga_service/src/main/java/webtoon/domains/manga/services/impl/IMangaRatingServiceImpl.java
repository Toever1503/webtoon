package webtoon.domains.manga.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import webtoon.domains.manga.dtos.MangaDto;
import webtoon.domains.manga.dtos.MangaRatingDto;
import webtoon.domains.manga.entities.MangaRatingEntity;
import webtoon.domains.manga.models.MangaRatingModel;
import webtoon.domains.manga.repositories.IMangaRatingRepository;
import webtoon.domains.manga.services.IMangaRatingService;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class IMangaRatingServiceImpl implements IMangaRatingService {

    private final IMangaRatingRepository ratingRepository;

    public IMangaRatingServiceImpl(IMangaRatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    @Override
    public MangaRatingDto add(MangaRatingModel model){
        MangaRatingEntity entity = MangaRatingEntity.builder()

                .id(model.getId())
                .rate(model.getRate())
                .createdBy(model.getCreatedBy())
                .mangaId(model.getMangaEntity())
                .build();
        this.ratingRepository.saveAndFlush(entity);
        return MangaRatingDto.builder()
                .mangaEntity(entity.getMangaId())
                .rate(entity.getRate())
                .build();
    }

    @Override
    public MangaRatingDto update(MangaRatingModel model){
        MangaRatingEntity entity = this.getById(model.getId());
        entity.setMangaId(model.getMangaEntity());
        entity.setRate(model.getRate());
        this.ratingRepository.saveAndFlush(entity);
        return MangaRatingDto.builder()
                .rate(entity.getRate())
                .mangaEntity(entity.getMangaId())
                .build();
    }

    @Override
    public MangaRatingEntity getById(Long id){
        return ratingRepository.findById(id).orElseThrow(() -> new RuntimeException("22"));
    }

    @Override
    public boolean deleteById(Long id){
        try {
            this.ratingRepository.deleteById(id);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public Page<MangaDto> findAllById(Long id){
        Pageable pageable = PageRequest.of(0, 2).withSort(Sort.Direction.DESC, "id");
        return ratingRepository.findAllById(id,pageable);
    }

    @Override
    public List<Map> getRating(Long id){

        return this.ratingRepository.getRateAvg(id);
    }




}
