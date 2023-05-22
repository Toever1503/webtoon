package webtoon.main.domains.manga.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import webtoon.main.account.entities.UserEntity;
import webtoon.main.domains.manga.dtos.MangaDto;
import webtoon.main.domains.manga.dtos.MangaRatingDto;
import webtoon.main.domains.manga.entities.MangaEntity;
import webtoon.main.domains.manga.entities.MangaRatingEntity;
import webtoon.main.domains.manga.models.MangaRatingModel;
import webtoon.main.domains.manga.repositories.IMangaRatingRepository;
import webtoon.main.domains.manga.repositories.IMangaRepository;
import webtoon.main.domains.manga.services.IMangaRatingService;
import webtoon.main.domains.manga.services.IMangaService;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class IMangaRatingServiceImpl implements IMangaRatingService {

    private final IMangaRatingRepository ratingRepository;
    @Autowired
    @Lazy
    private  IMangaService mangaService;

    private final IMangaRepository mangaRepository;

    public IMangaRatingServiceImpl(IMangaRatingRepository ratingRepository,  IMangaRepository mangaRepository) {
        this.ratingRepository = ratingRepository;
        this.mangaRepository = mangaRepository;
    }


    @Override
    public MangaRatingDto add(MangaRatingModel model, HttpSession session){
        UserEntity user = (UserEntity) session.getAttribute("loggedUser");
        if (user == null) {

            // Nếu người dùng chưa đăng nhập, lưu trữ thông tin đánh giá vào Local Storage thay vì lưu trữ trong phiên.
            // Viết mã xử lý tại đây, ví dụ:
            return null;
        }else {
            System.out.println(model.getMangaEntity() + "manga" + user.getId());
            MangaRatingEntity mangaRatingCheck = this.getById(model.getMangaEntity(), user.getId());

            if (mangaRatingCheck != null ){
                mangaRatingCheck.setRate(Float.valueOf(model.getRate()));
                this.ratingRepository.saveAndFlush(mangaRatingCheck);
                Double newRate =  this.ratingRepository.findRatingByMangaAndCb(mangaRatingCheck.getMangaId());
                if (newRate != null){
                    MangaEntity mangaEntity = this.mangaService.getById(mangaRatingCheck.getMangaId());
                    mangaEntity.setRating(newRate.floatValue());
                    this.mangaRepository.saveAndFlush(mangaEntity);
                }

                return MangaRatingDto.builder()
                        .id(mangaRatingCheck.getId())
                        .rate(mangaRatingCheck.getRate())
                        .mangaEntity(mangaRatingCheck.getMangaId())
                        .build();
            }else {
                MangaRatingEntity entity = MangaRatingEntity.builder()
                        .id(model.getId())
                        .rate(Float.valueOf(model.getRate()))
                        .createdBy(user.getId())
                        .mangaId(model.getMangaEntity())
                        .build();
                this.ratingRepository.saveAndFlush(entity);
                Double newRate =  this.ratingRepository.findRatingByMangaAndCb(entity.getMangaId());
                if (newRate != null){
                    MangaEntity mangaEntity = this.mangaService.getById(model.getMangaEntity());
                    if (mangaEntity != null){
                        mangaEntity.setRating(newRate.floatValue());
                        this.mangaRepository.saveAndFlush(mangaEntity);
                    }

                }
                return MangaRatingDto.builder()
                        .id(entity.getId())
                        .mangaEntity(entity.getMangaId())
                        .rate(entity.getRate())
                        .createdBy(entity.getCreatedBy())
                        .build();
            }



        }

    }


    public MangaRatingEntity getById(Long id, Long createId){
        MangaRatingEntity mangaRating = this.ratingRepository.findByMangaIdAndCB(id,createId);
        return mangaRating ;

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

    @Override
    public Double findRatingByMangaId(Long id){
        return this.ratingRepository.findRatingByManga(id);
    }





}
