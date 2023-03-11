package webtoon.domain.specifications;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import webtoon.domain.entities.MangaEntity;
import webtoon.domain.models.MangaModel;

public class MangaSpecification {
	
//	public static Specification<MangaEntity> like(String q) {
//        String finalQ = "%" + q + "%";
//        return (root, query, cb) -> cb.or(
//                cb.like(root.get(CoursesEntity_.TITLE), finalQ),
//                cb.like(root.get(CoursesEntity_.DESCRIPTION), finalQ)
//        );
//    }
//	public static Specification<MangaEntity> filter(MangaModel model){
//		List<Specification> specs = new ArrayList<>();
//		if(model.getTitle() != null) {
//			specs.add(null)
//		}
//		
//	}
}
