package webtoon.domains.manga.specifications;

import org.springframework.data.jpa.domain.Specification;
import webtoon.account.entities.UserEntity;
import webtoon.domains.manga.entities.MangaEntity;
import webtoon.domains.manga.entities.MangaEntity_;
import webtoon.domains.manga.entities.MangaGenreEntity_;
import webtoon.domains.manga.enums.EMangaSTS;
import webtoon.domains.manga.enums.EStatus;
import webtoon.domains.manga.filters.MangaFilterModel;
import webtoon.domains.manga.models.MangaFilterInput;

import javax.persistence.criteria.Join;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SearchSpecification {
    public static Specification<MangaEntity> byYear(String year){
        return ((root, query, criteriaBuilder) -> {
//           new Date()
            return criteriaBuilder.like(
                    root.get(MangaEntity_.CREATED_AT),year + "%"
            );
        });
    }

    public static Specification<MangaEntity> byGener(Long generId){

        return ((root, query, criteriaBuilder) -> {
           return criteriaBuilder.equal(root.join(MangaEntity_.GENRES).get(MangaGenreEntity_.ID),generId);
        });
    }

    public static Specification<MangaEntity> byStatus(EMangaSTS sts){
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.equal(root.get(MangaEntity_.MANGA_STATUS), sts.name());
    }

    public static Specification<MangaEntity> like(String q){
        return (root, query, criteriaBuilder) -> {
            final String qq = "%" + q + "%";
            return criteriaBuilder.or(
                criteriaBuilder.like(root.get(MangaEntity_.TITLE),qq),
                criteriaBuilder.like(root.get(MangaEntity_.MANGA_NAME),qq)
            );
        };
    }
//    public static Specification<MangaEntity> byCreatedBy(Long createdBy){
//        return (root, query, criteriaBuilder) -> {
//            Join<MangaEntity, UserEntity> join = root.join(MangaEntity_.createdBy);
//            return criteriaBuilder.equal(join.get(U))
//        }
//    }


    public static Specification<MangaEntity> filter(MangaFilterInput model){
        List<Specification> specs = new ArrayList<>();

        if (model.getQ() != null){
            specs.add(like(model.getQ()));
        }
        if(model.getStatus() != null){
            specs.add(byStatus(model.getStatus()));
        }
        if (model.getGenerId() != null){
            specs.add(byGener(model.getGenerId()));
        }
//        if (model.getReleaseYear() != null){
//            specs.add(byYear(model.getReleaseYear()));
//        }
        Specification<MangaEntity> finalSpec = null;
        for (Specification<MangaEntity> spec : specs ){
            if (finalSpec == null){
                finalSpec = Specification.where(spec);
            }else {
                finalSpec = finalSpec.and(spec);
            }
        }
        return finalSpec;
    }
}
