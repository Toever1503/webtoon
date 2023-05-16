package webtoon.main.domains.manga.specifications;

import org.springframework.data.jpa.domain.Specification;
import webtoon.main.account.entities.UserEntity;
import webtoon.main.domains.manga.entities.MangaEntity;
import webtoon.main.domains.manga.entities.MangaEntity_;
import webtoon.main.domains.manga.entities.MangaGenreEntity_;
import webtoon.main.domains.manga.enums.EMangaSTS;
import webtoon.main.domains.manga.enums.EStatus;
import webtoon.main.domains.manga.filters.MangaFilterModel;
import webtoon.main.domains.manga.models.MangaFilterInput;

import javax.persistence.criteria.Join;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SearchSpecification {
    public static Specification<MangaEntity> byYear(String year){
        return ((root, query, criteriaBuilder) -> {
           new Date();
            return criteriaBuilder.like(
                    root.get(MangaEntity_.CREATED_AT),"%" + year + "%"
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
            criteriaBuilder.equal(root.get(MangaEntity_.MANGA_STATUS),sts);
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

    public static Specification<MangaEntity> filter(MangaFilterInput model){
        List<Specification> specs = new ArrayList<>();

        if (model.getQ() != null){
            specs.add(like(model.getQ()));
        }
        if(model.getStatus() != null){
            specs.add(byStatus(model.getStatus()));
        }
        if (model.getGenreId() != null){
            specs.add(byGener(model.getGenreId()));
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
