package webtoon.main.payment.specifications;

import org.springframework.data.jpa.domain.Specification;
import webtoon.main.payment.entities.OrderEntity;
import webtoon.main.payment.entities.OrderEntity_;

public class SearchOrderSpecification {

    public static Specification<OrderEntity> like (String q) {
        return (root,query,criterBuilder)->{
            final String finalQ = "%" + q + "%";
            return criterBuilder.or(
                    criterBuilder.like(root.get(OrderEntity_.SUBS_PACK_ID), finalQ),
                    criterBuilder.like(root.get(OrderEntity_.MA_DON_HANG), finalQ)
            );
        };
    }
}
