package webtoon.payment.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import webtoon.payment.entities.SubscriptionPackEntity;

@Repository
public interface ISubscriptionPackRepository extends JpaRepository<SubscriptionPackEntity, Long>, JpaSpecificationExecutor<SubscriptionPackEntity> {

    @Query("SELECT s FROM tbl_subscription_pack s WHERE s.price = ?1")
    SubscriptionPackEntity getByPrice(Double price);
}
